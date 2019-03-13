package lab2.reqreply;

import lab2.utils.Utils;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Message;

/**
 * @author Peter Bromander, peter.bromander@dogfish.se
 */
public class Server {

    public static void main(String[] args) {
        System.out.println("Starting " + Server.class.getName());
        new Server().processRequests();
    }

    private void processRequests() {
        String queueName = "LAB2.Q1";
        try (JMSContext context = Utils.getContext()) {

            JMSConsumer consumer = context.createConsumer(context.createQueue(queueName));
            JMSProducer producer = context.createProducer();

            while (true) {
                Message requestMessage = consumer.receive();
                Message replyMessage = context.createTextMessage("Hello from server");
                replyMessage.setJMSCorrelationID(requestMessage.getJMSMessageID());

                producer.send(requestMessage.getJMSReplyTo(), replyMessage);
                System.out.println("Sent reply to " + requestMessage.getJMSReplyTo());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
