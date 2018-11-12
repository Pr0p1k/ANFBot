package P3212.ANFBot;

import com.rabbitmq.jms.admin.RMQConnectionFactory;
import com.rabbitmq.jms.client.RMQConnection;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.Session;

public abstract class JmsBase {
    private String host = "localhost";
    private int port = 1414;
    private String channel = "SYSTEM.DEF.SVRCONN";
    private String queueManagerName = "host1/qm1";
    private String queueName = "queue1";
    private boolean clientTransport = false;

    protected Session session = null;
    protected Queue destination = null;

    protected Connection connection = null;
    // статус выполнения приложения
    protected static int status = 1;

    protected abstract void doAction();

    //---------------------------------------------------------------
    private void createConnection() {
        try {
            RMQConnectionFactory factory = new RMQConnectionFactory();
            factory.setHost(host);
            connection = factory.createConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //---------------------------------------------------------------
    public JmsBase() {
        try {
            createConnection();
            session = connection.createSession(false,
                    Session.AUTO_ACKNOWLEDGE);
            destination = session.createQueue(queueName);
            // Старт подключения
            connection.start();
            // Выполнение действий
            doAction();
        } catch (JMSException jmsex) {
            recordFailure(jmsex);
        } finally {
            try {
                if (session != null)
                    session.close();
                if (connection != null)
                    connection.close();
            } catch (JMSException jmsex) {
                System.err.println("" + jmsex.getMessage());
                recordFailure(jmsex);
            }
        }
    }

    //---------------------------------------------------------------
    protected void recordFailure(Exception ex) {
        // ...
    }

    //---------------------------------------------------------------
    protected void processJMSException(JMSException jmse) {
        // ...
    }
}