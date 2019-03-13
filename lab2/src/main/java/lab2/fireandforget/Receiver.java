package lab2.fireandforget;

import com.ibm.jms.JMSTextMessage;
import lab2.utils.Utils;

import javax.jms.*;

/**
 * Created on 2019-02-20.
 *
 * @author Peter Bromander, peter.bromander@dogfish.se
 */
public class Receiver {
    public static void main(String[] args) throws Exception {
        System.out.println("Starting " + Receiver.class.getName());
        new Receiver().receiver20();
    }

    private void receiver11() throws JMSException {
        Connection connection = Utils.getConnection2();
        try (
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                MessageConsumer consumer = session.createConsumer(session.createQueue("LAB2.Q1"))) {
            connection.start();
            System.out.println("Waiting for message");

            while (true) {
                try {
                    Message message = consumer.receive();
                    if (message instanceof JMSTextMessage)
                        System.out.println("Got message " + ((JMSTextMessage) message).getText());
                    else
                        System.out.println("Got message of type " + message.getJMSType());
                } catch (Exception x) {
                    System.out.print(".");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void receiver20() throws InterruptedException {
        while (true) {
            try (JMSContext context = Utils.getContext();
                 JMSConsumer consumer = context.createConsumer(context.createQueue("LAB2.Q1"))) {

                System.out.println("Waiting for message");
                Message message = consumer.receive();

                System.out.println("Got message " + message.getBody(String.class));

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
