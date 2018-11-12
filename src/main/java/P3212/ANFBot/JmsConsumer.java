package P3212.ANFBot;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.TextMessage;

public class JmsConsumer extends JmsBase
{
    private  final  int      timeout  = 30000;
    private MessageConsumer consumer = null;

    @Override
    protected void doAction()
    {
        Message message;
        try {
            consumer = session.createConsumer(destination);
            do {
                message = consumer.receive(timeout);
                if (message != null)
                    System.out.println("\nСообщение :\n" + ((TextMessage) message).getText());
            } while (message != null);
        } catch (JMSException e) {
            e.printStackTrace();
        }
        System.out.println("\nПросмотр JMS сообщений завершен\n");
    }
    public JmsConsumer()
    {
        super();
        try {
            if (consumer != null)
                consumer.close();
        } catch (JMSException jmsex) {
            recordFailure(jmsex);
        }
    }
    public static void main(String[] args)
    {
        new JmsConsumer();
        System.exit(status);
    }
}