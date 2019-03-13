package lab2.tx;

import com.ibm.msg.client.jms.JmsConnectionFactory;
import lab2.utils.Utils;

import javax.jms.*;

/**
 *
 * @author Peter Bromander, peter.bromander@dogfish.se
 */
public class Sender {
    public static void main(String[] args) throws JMSException {
        System.out.println("Starting " + Sender.class.getName());
        new Sender().send();
    }


    /**
     *
     */
    private void send() throws JMSException {
        String queueName = "LAB2.Q1";
        /*
        Session.AUTO_ACKNOWLEDGE;
        Session.CLIENT_ACKNOWLEDGE;
        Session.DUPS_OK_ACKNOWLEDGE;
        Session.SESSION_TRANSACTED;
        */
        JmsConnectionFactory cf = Utils.getJmsConnectionFactory();
        try (JMSContext context = cf.createContext()) {
            System.out.println("Session is transacted " + context.getTransacted());
            Message message = context.createTextMessage("Hello");
            context.createProducer().send(context.createQueue(queueName), message);

            System.out.println("Message sent to queue " + queueName + " id = " + message.getJMSMessageID());
            //context.commit();
        } catch (JMSRuntimeException e) {
            e.printStackTrace();
        }
    }
}
