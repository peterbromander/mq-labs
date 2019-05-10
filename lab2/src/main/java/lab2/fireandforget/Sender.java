package lab2.fireandforget;

import lab2.utils.Utils;

import javax.jms.*;

/**
 * Created on 2019-02-20.
 *
 * @author Peter Bromander, peter.bromander@dogfish.se
 */
public class Sender {
    public static void main(String[] args) {
        System.out.println("Starting " + Sender.class.getName());
        new Sender().sendJMS20();
    }

    private void send11() {
        String queueName = "LAB2.Q1";

        // Create JMS session and JMS producer.
        try (Connection connection = Utils.getJmsConnectionFactory().createConnection();
             Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
             MessageProducer producer = session.createProducer(session.createQueue(queueName))) {

            // Send a message
            producer.send(session.createTextMessage("Hello!"));
            System.out.println("Message sent to queue " + queueName);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    private void sendJMS20() {
        String queueName = "LAB2.Q1";
        try (JMSContext context = Utils.getJmsConnectionFactory().createContext()) {
            context.createProducer().setDeliveryMode(DeliveryMode.PERSISTENT).send(context.createQueue(queueName), "Hello");
            System.out.println("Message sent using JMS20 to queue " + queueName);
        } catch (JMSRuntimeException | JMSException e) {
            e.printStackTrace();
        }
    }
}
