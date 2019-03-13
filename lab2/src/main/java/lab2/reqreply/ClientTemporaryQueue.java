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
        String queueName = "LAB2.Q1";

        // Create JMS session and JMS producer.
        try (JMSContext context = Utils.getContext())
        {
            JMSProducer producer = context.createProducer();
            Message request = context.createTextMessage("Hello!");
            Destination replyQueue = context.createTemporaryQueue();
            //
            request.setJMSReplyTo(replyQueue);
            // Send a message
            producer.send(context.createQueue(queueName),request);
            System.out.println("Message sent to queue " + queueName);

            JMSConsumer consumer = context.createConsumer(replyQueue);

            Message responseMessage = consumer.receive();
            System.out.println("Got response " + responseMessage.getJMSMessageID());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
