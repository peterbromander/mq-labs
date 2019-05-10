package lab2.utils;


import com.ibm.msg.client.jms.JmsConnectionFactory;
import com.ibm.msg.client.jms.JmsFactoryFactory;
import com.ibm.msg.client.wmq.WMQConstants;

import javax.jms.JMSException;
import javax.jms.Message;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Random;

/**
 *
 * @author Peter Bromander, peter.bromander@dogfish.se
 */
public class Utils {

    Properties prop = new Properties();

    public static byte[] generateRandomBytes(int size) {
        byte[] randomSeq = new byte[size];
        new Random().nextBytes(randomSeq);
        return randomSeq;
    }

    public void init() throws IOException {
        prop.load(new FileInputStream("Application.properties"));
    }

    /**
     * This returns a JmsConnectionFactory, it is recommended that you use JMS2.0 jmsConnectionFactory.createContext()
     *
     * ** DO NOT ** specify the queue manager name.
     *
     * If you need SSL see comments below.
     * @return The factory
     * @throws JMSException
     */
    public static JmsConnectionFactory getJmsConnectionFactory() throws JMSException {
        JmsFactoryFactory ff = JmsFactoryFactory.getInstance(WMQConstants.WMQ_PROVIDER);
        JmsConnectionFactory cf = ff.createConnectionFactory();
        cf.setStringProperty(WMQConstants.WMQ_HOST_NAME, "127.0.0.1");
        cf.setIntProperty(WMQConstants.WMQ_PORT, 1414);
        cf.setStringProperty(WMQConstants.WMQ_CHANNEL, "DEV.APP.SVRCONN");
        cf.setIntProperty(WMQConstants.WMQ_CONNECTION_MODE, WMQConstants.WMQ_CM_CLIENT);
        cf.setIntProperty(WMQConstants.WMQ_CLIENT_RECONNECT_OPTIONS, WMQConstants.WMQ_CLIENT_RECONNECT_Q_MGR);
        /*
         * To use SSL you need to do the following:
         *
         * 1. Un comment the line below, The actual cipher value needs to bee coordinated with the MQ operations
         */

       //  cf.setStringProperty(WMQConstants.WMQ_SSL_CIPHER_SUITE,"TLS_RSA_WITH_AES_128_CBC_SHA");

        /*
         * 2. If your not running a IBM JRE you need to specify
         * -Dcom.ibm.mq.cfg.useIBMCipherMappings=false
         *
         *
         *
         * 3. The you need to point to your JKS-file which should be both trust- and key-store .
         * -Djavax.net.ssl.keyStore=/.../your.jks
         * -Djavax.net.ssl.trustStore=/.../your.jks
         * -Djavax.net.ssl.keyStorePassword=*****
         * -Djavax.net.ssl.trustStorePassword=****
         *
         *
         * If you still get error you can debug the SSL using
         * -Djavax.net.debug=all
         */
        return cf;
    }

    public static void printJMSProperties(Message message) throws JMSException {
        System.out.println("MID     \t" + message.getJMSMessageID());
        System.out.println("CID     \t" + message.getJMSCorrelationID());
        System.out.println("RepylTo \t" + message.getJMSReplyTo());
        System.out.println("Expiry  \t" + message.getJMSExpiration());
        System.out.println("Delivery mode\t" + message.getJMSDeliveryMode());
        System.out.println("Prio    \t" + message.getJMSPriority());
        System.out.println("Type    \t" + message.getJMSType());
        System.out.println("Class    \t" + message.getClass().getName());
        System.out.println(" -------- ");

        @SuppressWarnings("rawtypes")
        Enumeration e = message.getPropertyNames();
        while (e.hasMoreElements()) {
            String name = (String) e.nextElement();
            System.out.println(name + " \t" + message.getStringProperty(name));
        }
    }
}
