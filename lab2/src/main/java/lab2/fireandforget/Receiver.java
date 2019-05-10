package lab2.fireandforget;

import com.ibm.jms.JMSTextMessage;
import com.ibm.msg.client.jms.JmsConnectionFactory;
import lab2.utils.Utils;

import javax.jms.*;
import javax.xml.stream.FactoryConfigurationError;

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
        Connection connection = Utils.getJmsConnectionFactory().createConnection();
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

    private void receiver20() throws InterruptedException, JMSException {
        JmsConnectionFactory cf= Utils.getJmsConnectionFactory();
        while (true) {
            try (JMSContext context = cf.createContext(Session.SESSION_TRANSACTED);
                 JMSConsumer consumer = context.createConsumer(context.createQueue("LAB2.Q1"))) {


                System.out.println("Waiting for message");
                Message message = consumer.receive();

                Utils.printJMSProperties(message);

                System.out.println("Got message " + message.getBody(String.class));

               // throw new RuntimeException("Failed");
                context.commit();

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
