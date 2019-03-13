package lab2.reqreply;

import lab2.utils.Utils;

import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.Message;

/**
 * @author Peter Bromander, peter.bromander@dogfish.se
 */
public class ClientCorrelationId {

    public static void main(String[] args) {
        System.out.println("Starting " + ClientCorrelationId.class.getName());
        new ClientCorrelationId().makeRequest();
    }

    private void makeRequest() {
        String queueName = "LAB2.Q1";

        // Create JMS session and JMS producer.
        try (JMSContext connection = Utils.getContext()) {

            Message request = connection.createTextMessage("Hello!");

            Destination replyQueue = connection.createQueue("LAB2.Q2");

            Destination requestQueue = connection.createQueue("LAB2.Q1");

            request.setJMSReplyTo(replyQueue);

            // Send a message
            connection.createProducer().send(requestQueue, request);

            System.out.println("Message sent to queue " + queueName);

            JMSConsumer consumer = connection.createConsumer(replyQueue, "JMSCorrelationID='" + request.getJMSMessageID() + "'");

            Message responseMessage = consumer.receive();
            System.out.println("Got response " + responseMessage.getJMSMessageID());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
