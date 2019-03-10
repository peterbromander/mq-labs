package lab2.reqreply;

import lab2.utils.Utils;

import javax.jms.*;

/**
 * @author Peter Bromander, peter.bromander@dogfish.se
 */
public class ClientTemporaryQueue {

    public static void main(String[] args) {
        System.out.println("Starting " + ClientTemporaryQueue.class.getName());
        new ClientTemporaryQueue().makeRequest();
    }

    private void makeRequest() {
        String queueName = "Q1";

        // Create JMS session and JMS producer.
        try (Connection connection = Utils.getConnection();
             Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
             MessageProducer producer = session.createProducer(session.createQueue(queueName))) {

            Message request = session.createTextMessage("Hello!");

            Destination replyQueue = session.createTemporaryQueue();

            //
            request.setJMSReplyTo(replyQueue);
            // Send a message
            producer.send(request);
            System.out.println("Message sent to queue " + queueName);

            MessageConsumer consumer = session.createConsumer(replyQueue);

            connection.start();

            Message responseMessage = consumer.receive();
            System.out.println("Got response " + responseMessage.getJMSMessageID());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
