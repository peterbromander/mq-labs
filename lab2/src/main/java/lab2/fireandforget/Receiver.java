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
        System.out.println("Starting fire and forget sender...");
        new Receiver().receiver11();
    }

    private void receiver11() throws JMSException {
        Connection connection = Utils.getConnection2();
        try (
             Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
             MessageConsumer consumer = session.createConsumer(session.createQueue("Q1"))) {
            connection.start();
            System.out.println("Waiting for message");

            while (true)  {
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
            try (Connection connection = Utils.getConnection();
                 Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                 MessageConsumer consumer = session.createConsumer(session.createQueue("Q1"))) {
                connection.start();
                System.out.println("Waiting for message");
                Message message = consumer.receive();
                if (message instanceof JMSTextMessage)
                    System.out.println("Got message " + ((JMSTextMessage) message).getText());
                else
                    System.out.println("Got message of type " + message.getJMSType());
            } catch (Exception e) {
                //e.printStackTrace();
                System.out.println(e.getMessage());
                Thread.sleep(1000*5);
            }
        }
    }
}
