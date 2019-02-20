package lab3.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.listener.SessionAwareMessageListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.*;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created on 2019-02-20.
 *
 * @author Peter Bromander, peter.bromander@dogfish.se
 */
@Component
public class Idempotent implements SessionAwareMessageListener {

    private static final Logger LOG = LoggerFactory.getLogger(Idempotent.class);

    static String TARGET_KEY = "TARGET_QUEUE";

    static String MESSAGE_HASH_KEY = "MESSAGE_HASH";

    static String MESSAGE_TTL_KEY = "MESSAGE_RETENTION";

    @Value("${proxy.state-queue}")
    private String STATE_QUEUE;

    @Value("${proxy.retention-hours}")
    private int STATE_RETENTION_HOURS;


    @Override
    @JmsListener(destination = "${proxy.receive-queue}")
    @Transactional
    public void onMessage(Message message, Session session) throws JMSException {
        LOG.info("OnlyOnce - Got message");
        try {
            processMessage(message, session);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to process message", e);
        }
    }

    private void processMessage(Message message, Session session) throws JMSException, NoSuchAlgorithmException {
        String hash = messageHash(message);
        try (MessageProducer producerTarget = session.createProducer(session.createQueue(message.getStringProperty(TARGET_KEY)));
             MessageProducer producerState = session.createProducer(session.createQueue(STATE_QUEUE));
             QueueBrowser browserState = session.createBrowser(session.createQueue(STATE_QUEUE), "JMSCorrelationID='ID:" + hash + "'")) {

            if (!browserState.getEnumeration().hasMoreElements()) {

                Message stateMessage = session.createTextMessage("State message : " + hash);
                stateMessage.setJMSCorrelationID("ID:" + hash);
                if (message.getStringProperty(MESSAGE_TTL_KEY) != null) {
                    STATE_RETENTION_HOURS = Integer.parseInt(message.getStringProperty(MESSAGE_TTL_KEY));
                }
                producerState.setTimeToLive(1000 * 60 * 60 * STATE_RETENTION_HOURS);
                producerState.setDeliveryMode(DeliveryMode.PERSISTENT);
                producerState.send(stateMessage);

                producerTarget.setDeliveryMode(DeliveryMode.PERSISTENT);
                producerTarget.send(message);

                LOG.info("hash={}, JMS_MESSAGE_HASH='{}', action='Sent to queue={}', msgid={}, retention={}", hash, message.getStringProperty(MESSAGE_HASH_KEY), message.getStringProperty(TARGET_KEY), message.getJMSMessageID(), STATE_RETENTION_HOURS);
            } else {
                LOG.warn("hash={}, JMS_MESSAGE_HASH='{}', action='Already sent - discarded',  msg={}", hash, message.getStringProperty(MESSAGE_HASH_KEY), ((TextMessage) message).getText());
            }
        }
    }

    private String hash(TextMessage message) throws NoSuchAlgorithmException, JMSException {
        MessageDigest md = MessageDigest.getInstance("SHA");
        String queueName = message.getStringProperty(TARGET_KEY);
        String existingHash = message.getStringProperty(MESSAGE_HASH_KEY);

        if (existingHash != null && existingHash.length() > 1) {
            md.update((existingHash + queueName).getBytes());
        } else {
            md.update((message.getText() + queueName).getBytes());
        }

        byte[] hash = md.digest();
        return DatatypeConverter.printHexBinary(hash) + "00000000";
    }

    private String messageHash(Message message) throws JMSException, NoSuchAlgorithmException {
        if (message instanceof TextMessage)
            return hash((TextMessage) message);
        throw new IllegalArgumentException("Unsupported messages type " + message.getClass().getName());
    }
}