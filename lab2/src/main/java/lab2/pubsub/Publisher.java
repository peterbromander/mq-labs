package lab2.pubsub;

import lab2.reqreply.ClientCorrelationId;

/**
 *
 * @author Peter Bromander, peter.bromander@dogfish.se
 */
public class Publisher {

    public static void main(String[] args) {
        System.out.println("Starting "+ Publisher.class.getName());
        new Publisher().publish();
    }

    private void publish() {
        
    }


}
