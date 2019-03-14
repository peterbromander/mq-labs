package lab2.pubsub;

import com.ibm.msg.client.jms.JmsConstants;
import lab2.utils.Utils;

import javax.jms.*;
import java.util.Date;

/**
 * @author Peter Bromander, peter.bromander@dogfish.se
 */
public class Publisher {

    public static void main(String[] args) {
        System.out.println("Starting " + Publisher.class.getName());
        new Publisher().publish();
    }

    private void publish() {
        try (JMSContext context = Utils.getContext()) {
            Topic topic = context.createTopic("dev/");
            JMSProducer producer = context.createProducer();
            producer.send(topic, "Hello");
            System.out.println("Sent publication to topic " + topic.getTopicName());
        } catch (JMSRuntimeException | JMSException e) {
            e.printStackTrace();
        }
    }

    //

    private void publishRetained() {
        try (JMSContext context = Utils.getContext()) {
            Topic topic = context.createTopic("dev/");
            JMSProducer producer = context.createProducer();

            Message message = context.createTextMessage("Hello retained " + new Date());
            message.setIntProperty(JmsConstants.JMS_IBM_RETAIN, JmsConstants.RETAIN_PUBLICATION);

            producer.send(topic, message);
            System.out.println("Sent retained publication to topic " + topic.getTopicName());
        } catch (JMSRuntimeException | JMSException e) {
            e.printStackTrace();
        }
    }

}
