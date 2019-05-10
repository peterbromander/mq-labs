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
    public void onMessage(Message message, Session session) throws RuntimeException {
        try {
            processMessage(message, session);
        } catch (Exception e) {
            throw new RuntimeException("Failed to process message - " + e.getMessage());
        }
    }

    private void processMessage(Message message, Session session) throws RuntimeException, JMSException, NoSuchAlgorithmException {
        //String targetQueue=message.getStringProperty(TARGET_KEY);
        String targetQueue = "LAB3.OUT";

        String hash = hash(message, targetQueue);
        try (MessageProducer producerTarget = session.createProducer(session.createQueue(targetQueue));
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

                LOG.info("hash={}, JMS_MESSAGE_HASH='{}', action='Sent to queue={}', msgid={}, retention={}", hash, message.getStringProperty(MESSAGE_HASH_KEY), targetQueue, message.getJMSMessageID(), STATE_RETENTION_HOURS);
            } else {
                LOG.warn("hash={}, JMS_MESSAGE_HASH='{}', action='Already sent - discarded',  msg={}", hash, message.getStringProperty(MESSAGE_HASH_KEY), ((TextMessage) message).getText());
            }
        } catch (JMSException e) {
            throw new RuntimeException("Failed to process message" + e.getLinkedException().getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to process message" + e.getMessage(), e);
        }
    }

    private String hash(Message message, String queueName) throws NoSuchAlgorithmException, JMSException {
        MessageDigest md = MessageDigest.getInstance("SHA");

        if (queueName == null) throw new RuntimeException("Message do not contain target queue");

        String existingHash = message.getStringProperty(MESSAGE_HASH_KEY);

        if (existingHash != null && existingHash.length() > 1) {
            md.update((existingHash + queueName).getBytes());
        } else {
            md.update((message.getBody(String.class) + queueName).getBytes());
        }
        return DatatypeConverter.printHexBinary(md.digest()) + "00000000";
    }
}