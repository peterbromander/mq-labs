package lab2.seg;


/**
 *
 * @author Peter Bromander, peter.bromander@dogfish.se
 */
public class Segments {
    private void run(){
        /*
        try {
            // perform connection
            manager = getManager(managerFactory);

            // put oversize message
            try (final MessageProducer producer = new MessageProducerImpl(QUEUE_NAME, manager)) {

                InputStream stream = new ByteArrayInputStream(bigBlock);

                MQMessage sentMessage = producer.send(stream, new MessageSetup() {
                    @Override
                    public void setup(MQMessage message) {
                        message.messageFlags = MQMF_SEGMENTATION_ALLOWED; // THIS IS IMPORTANT!
                    }

                    @Override
                    public void setup(MQPutMessageOptions putOptions) {
                        putOptions.options = MQPMO_NONE | MQPMO_NEW_MSG_ID | MQPMO_NO_SYNCPOINT;
                    }
                });
                sentMessageID = sentMessage.messageId;
                LOG.info(String.format("Sent message %s", HexConverter.bytesToHex(sentMessage.messageId)));
            }

            // get oversize message
            try(final MessageConsumer consumer = new MessageConsumerImpl(QUEUE_NAME, manager)) {
                MQMessage gotMessage = consumer.get(new MessageSetup() {
                    @Override
                    public void setup(MQGetMessageOptions getOptions) {
                        getOptions.options = MQGMO_NONE | MQGMO_COMPLETE_MSG;
                    }
                });
                gotMessageID = gotMessage.messageId;

                assertArrayEquals(sentMessageID, gotMessageID);

                //
                int bodyLength = gotMessage.getDataLength();
                assertThat("Body has wrong size", bodyLength, is(BIG_BLOCK));

                byte[] body = new byte[bodyLength];
                gotMessage.readFully(body);


                LOG.info(String.format("Got message %s", HexConverter.bytesToHex(gotMessage.messageId)));
            }

        } finally {
            close(manager);
        }*/
    }
}
