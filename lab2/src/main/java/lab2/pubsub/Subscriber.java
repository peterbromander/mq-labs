package lab2.pubsub;

import lab2.utils.Utils;

import javax.jms.*;

/**
 * @author Peter Bromander, peter.bromander@dogfish.se
 */
public class Subscriber {
    public static void main(String[] args) throws Exception {
        System.out.println("Starting " + Subscriber.class.getName());
        new Subscriber().subscribe();
    }

    private void subscribe() {
        try (JMSContext context = Utils.getContext()) {
            Topic topic = context.createTopic("dev/");
            JMSConsumer consumer = context.createConsumer(topic);
            System.out.println("Subscribe to topic " + topic.getTopicName());
            Message message = consumer.receive();
            System.out.println("Received publication " + message.getBody(String.class));
        } catch (JMSRuntimeException | JMSException e) {
            e.printStackTrace();
        }
    }

    private void subscribeDurable() {
        try (JMSContext context = Utils.getContext()) {
            Topic topic = context.createTopic("dev/");

            // We need to set the ID when using durable subscriptions.
            context.setClientID("ID1");

            // We also need to give the subscription a name e.g. 's1'
            JMSConsumer consumer = context.createDurableConsumer(topic,"s1");

            System.out.println("Durable subscribe to topic " + topic.getTopicName());
            Message message = consumer.receive(1000);
            System.out.println("Received publication " + (message!=null?message.getBody(String.class):"none"));

            /*
            consumer.close();
            context.unsubscribe("s1");
            System.out.println("Unsubscribed");
            */
        } catch (JMSRuntimeException | JMSException e) {
            e.printStackTrace();
        }
    }

}
