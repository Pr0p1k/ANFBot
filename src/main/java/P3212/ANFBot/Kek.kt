package P3212.ANFBot

import com.rabbitmq.jms.admin.RMQConnectionFactory
import javax.jms.JMSException
import javax.jms.MessageProducer
import javax.jms.Session
import javax.jms.TextMessage

object Kek {
    private fun request(msg: String, chatId: Long) {
        val factory = RMQConnectionFactory()
        factory.host = "localhost"
        val con = factory.createConnection()
        val session = con.createSession(false, Session.AUTO_ACKNOWLEDGE)
        val destination = session.createQueue("botQueue")
        val producer = session.createProducer(destination)
        var message = session.createTextMessage(msg)
        producer.send(message)

        val consumer = session.createConsumer(destination)
        val response = consumer.receive(10000)
        println(response)
    }
    @JvmStatic
    fun main(args: Array<String>) {
        request("lel", -1)
    }
}
