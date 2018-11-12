package P3212.ANFBot;

import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.TextMessage;

public class Kek extends JmsBase {
    private MessageProducer producer = null;

    public Kek()
    {
        super();
        try {
            if (producer != null)
                producer.close();
        } catch (JMSException jmse) {
            recordFailure(jmse);
        }
    }
    @Override
    protected void doAction()
    {
        // Создание текстовых сообщений
        String[] messages = new String[2];
        messages[0] = "Good day";
        messages[1] = "Hello, world!";
        String TEMPL = "Cообщение #%d отправлено";
        try {
            producer = session.createProducer(destination);
            System.out.println("Отправка JMS сообщений\n");
            TextMessage message = null;
            for (int i = 0; i < messages.length; i++) {
                message = session.createTextMessage(messages[i]);
                producer.send(message);
                System.out.println(String.format(TEMPL, (i + 1)));
            }
        } catch (JMSException e) {
            status = -2;
            recordFailure(e);
        }
    }
    public static void main(String[] args)
    {
        new Kek();
        System.exit(status);
    }
}
