package lab2.seg;

import lab2.utils.Utils;

import javax.jms.*;
import java.math.BigInteger;
import java.util.Random;

/**
 *
 * @author Peter Bromander, peter.bromander@dogfish.se
 */
public class Groups {

    public static void main(String[] args) throws JMSException {
        System.out.println("Starting " + Groups.class.getName());
        new Groups().putBigMessage();
    }

    public void putBigMessage() throws JMSException {

        String QUEUE_NAME="LAB2.Q1";
        final int BIG_BLOCK = 32768;

        final int SMALL_BLOCK = 256; // REMEMBER: JMS, RFH2 and other headers has a weight too! DON'T SET MAX ALLOWED BODY SIZE!

        byte[] bigBlock = Utils.generateRandomBytes(BIG_BLOCK);

        JMSContext context = Utils.getContext();
        Queue queue = context.createQueue(QUEUE_NAME);
        JMSProducer producer = context.createProducer();
        final String groupId = "ID:" + new BigInteger(24 * 8, new Random()).toString(16);

        final int groups = BIG_BLOCK / SMALL_BLOCK;

        // Write group
        for (int z = 0; z < groups; z++) {
            final BytesMessage message = context.createBytesMessage();
            byte[] smallBlock = new byte[SMALL_BLOCK];
            System.arraycopy(bigBlock, z * SMALL_BLOCK, smallBlock, 0, SMALL_BLOCK);
            message.writeBytes(smallBlock);
            message.setStringProperty("JMSXGroupID", groupId);
            message.setIntProperty("JMSXGroupSeq", z + 1); // first value is 1
            if (z == (groups - 1)) {
                message.setBooleanProperty("JMS_IBM_Last_Msg_In_Group", true);
            }
            producer.send(queue,message);
        }


        // assembly group back
        JMSConsumer lastMessageConsumer = context.createConsumer(queue, "JMS_IBM_Last_Msg_In_Group=TRUE");
        BytesMessage lastMessage = (BytesMessage) lastMessageConsumer.receiveNoWait();

        final int receivedGroupSize = lastMessage.getIntProperty("JMSXGroupSeq");

        final String receivedGroupId = lastMessage.getStringProperty("JMSXGroupID");

        lastMessageConsumer.close();


        final byte[] recoveredBlocks = new byte[BIG_BLOCK];
/*
        for (int z = 1; z < receivedGroupSize; z++) {
             JMSConsumer consumer = context.createConsumer(queue, "JMSXGroupID='" + receivedGroupId + "'AND JMSXGroupSeq=" + z);
            BytesMessage message = (BytesMessage) consumer.receiveNoWait();
            long bodyLength = message.getBodyLength();
            byte[] bodyBuffer = new byte[(int) bodyLength];
            message.readBytes(bodyBuffer);
            System.arraycopy(bodyBuffer, 0, recoveredBlocks, (z - 1) * SMALL_BLOCK, SMALL_BLOCK);
        }

        long lastMessageBodyLen = lastMessage.getBodyLength();
        byte[] bodyBuffer = new byte[(int) lastMessageBodyLen];
        lastMessage.readBytes(bodyBuffer);
        System.arraycopy(bodyBuffer, 0, recoveredBlocks, (receivedGroupSize - 1) * SMALL_BLOCK, SMALL_BLOCK);
        */
    }
}
