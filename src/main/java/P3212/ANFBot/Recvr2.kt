package P3212.ANFBot

import com.rabbitmq.jms.admin.RMQConnectionFactory
import javax.annotation.Resource
import javax.jms.*


private fun request(msg: String, chatId: Long) {
    val factory = RMQConnectionFactory()
    factory.host = "localhost"
    val con = factory.createConnection()
    val session = con.createSession(false, Session.AUTO_ACKNOWLEDGE)
    val consumer = session.createConsumer(session.createQueue("bot"))
    con.start()
    val message = consumer.receive()
    println((message as TextMessage).text)
    val producer = session.createProducer(session.createQueue("botResponse"))
    val listener = object : Thread() {
        override fun run() {
            listen()
        }

        fun listen() {
            var message = consumer.receive()
            println((message as TextMessage).text)
            respond(message.text)
            listen()
        }

        fun respond(msg: String) {
            producer.send(session.createTextMessage(msg.toUpperCase()))
        }

    }
    listener.start()
//    producer.send(session.createTextMessage(message.text.toUpperCase()))
}


fun main(args: Array<String>) {
    request("lolsdf", -1)
}
