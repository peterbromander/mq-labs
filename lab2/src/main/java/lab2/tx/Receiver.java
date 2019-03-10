package lab2.tx;

import com.ibm.msg.client.jms.JmsConnectionFactory;
import lab2.utils.Utils;

import javax.jms.*;

/**
 *
 * @author Peter Bromander, peter.bromander@dogfish.se
 */
public class Receiver {
    public static void main(String[] args) throws JMSException {
        System.out.println("Starting " + Receiver.class.getName());
        new Receiver().receive();
    }



    /**
     *
     */
    private void receive() throws JMSException {
        String queueName = "Q1";

        JmsConnectionFactory cf = Utils.getJmsConnectionFactory();
        try (JMSContext context = cf.createContext(Session.CLIENT_ACKNOWLEDGE)) {
            System.out.println("Session is transacted " + context.getTransacted());

            Message message = context.createConsumer(context.createQueue(queueName)).receive();
            message.acknowledge();

            System.out.println("Message received from queue " + queueName + " id = " + message.getJMSMessageID());

        } catch (JMSRuntimeException e) {
            e.printStackTrace();
        }
    }
}
