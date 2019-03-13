package lab2.utils;


import com.ibm.mq.jms.MQConnectionFactory;
import com.ibm.msg.client.jms.JmsConnectionFactory;
import com.ibm.msg.client.jms.JmsFactoryFactory;
import com.ibm.msg.client.wmq.WMQConstants;

import javax.jms.Connection;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Random;

/**
 * Created on 2019-02-20.
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

    public static Connection getConnection() throws JMSException {
        MQConnectionFactory cf = new MQConnectionFactory();
        cf.setTransportType(WMQConstants.WMQ_CM_CLIENT);
        cf.setChannel("DEV.APP.SVRCONN");
        cf.setConnectionNameList("127.0.0.1(1414)");
        cf.setClientReconnectOptions(WMQConstants.WMQ_CLIENT_RECONNECT_Q_MGR);
        cf.setClientReconnectTimeout(600);
        return cf.createConnection();
    }


    public static Connection getConnection2() throws JMSException {
        JmsFactoryFactory ff = JmsFactoryFactory.getInstance(WMQConstants.WMQ_PROVIDER);
        JmsConnectionFactory cf = ff.createConnectionFactory();
        cf.setStringProperty(WMQConstants.WMQ_HOST_NAME, "127.0.0.1");
        cf.setIntProperty(WMQConstants.WMQ_PORT, 1414);
        cf.setStringProperty(WMQConstants.WMQ_CHANNEL, "DEV.APP.SVRCONN");
        cf.setIntProperty(WMQConstants.WMQ_CONNECTION_MODE, WMQConstants.WMQ_CM_CLIENT);
        cf.setIntProperty(WMQConstants.WMQ_CLIENT_RECONNECT_OPTIONS, WMQConstants.WMQ_CLIENT_RECONNECT_Q_MGR);
   //     cf.setStringProperty(WMQConstants.WMQ_QUEUE_MANAGER, "QM1");
       // cf.setStringProperty(WMQConstants.WMQ_APPLICATIONNAME, "JmsPutGet (JMS)");
       // cf.setBooleanProperty(WMQConstants.USER_AUTHENTICATION_MQCSP, true);
       // cf.setStringProperty(WMQConstants.USERID, APP_USER);
       // cf.setStringProperty(WMQConstants.PASSWORD, APP_PASSWORD);
        return cf.createConnection();
    }

    public static JMSContext getContext() throws JMSException {
        JmsFactoryFactory ff = JmsFactoryFactory.getInstance(WMQConstants.WMQ_PROVIDER);
        JmsConnectionFactory cf = ff.createConnectionFactory();
        cf.setStringProperty(WMQConstants.WMQ_HOST_NAME, "127.0.0.1");
        cf.setIntProperty(WMQConstants.WMQ_PORT, 1414);
        cf.setStringProperty(WMQConstants.WMQ_CHANNEL, "DEV.APP.SVRCONN");
        cf.setIntProperty(WMQConstants.WMQ_CONNECTION_MODE, WMQConstants.WMQ_CM_CLIENT);
        cf.setIntProperty(WMQConstants.WMQ_CLIENT_RECONNECT_OPTIONS, WMQConstants.WMQ_CLIENT_RECONNECT_Q_MGR);
  //      cf.setStringProperty(WMQConstants.WMQ_QUEUE_MANAGER, "QM1");
        return cf.createContext();
    }

    public static JmsConnectionFactory getJmsConnectionFactory() throws JMSException {
        JmsFactoryFactory ff = JmsFactoryFactory.getInstance(WMQConstants.WMQ_PROVIDER);
        JmsConnectionFactory cf = ff.createConnectionFactory();
        cf.setStringProperty(WMQConstants.WMQ_HOST_NAME, "127.0.0.1");
        cf.setIntProperty(WMQConstants.WMQ_PORT, 1414);
        cf.setStringProperty(WMQConstants.WMQ_CHANNEL, "DEV.APP.SVRCONN");
        cf.setIntProperty(WMQConstants.WMQ_CONNECTION_MODE, WMQConstants.WMQ_CM_CLIENT);
        cf.setIntProperty(WMQConstants.WMQ_CLIENT_RECONNECT_OPTIONS, WMQConstants.WMQ_CLIENT_RECONNECT_Q_MGR);
      //  cf.setStringProperty(WMQConstants.WMQ_QUEUE_MANAGER, "QM1");
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
