package lab2.fireandforget;

import lab2.utils.Connections;

import javax.jms.*;

/**
 * Created on 2019-02-20.
 *
 * @author Peter Bromander, peter.bromander@dogfish.se
 */
public class Sender {
    public static void main(String[] args) throws Exception {
        System.out.println("Starting fire and forget sender...");
        new Sender().send();
    }

    private void send() throws JMSException {
        String queueName = "Q1";
        Connection connection = Connections.getConnection();

        // Create JMS session and JMS producer.
        try (Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
             MessageProducer producer = session.createProducer(session.createQueue(queueName))) {

            // Send a message
            producer.send(session.createTextMessage("Hello!"));
            System.out.println("Message sent to queue " + queueName);
        }
    }
}
