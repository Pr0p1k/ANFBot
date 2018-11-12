package P3212.ANFBot

import com.rabbitmq.client.*
import com.rabbitmq.jms.admin.RMQConnectionFactory
import javax.annotation.Resource
import javax.jms.Destination
import javax.jms.MessageProducer
import javax.jms.Session

    fun main(args: Array<String>) {
        val factory = RMQConnectionFactory()

    }

    fun response() {
        val factory = ConnectionFactory()
        factory.host = "localhost"
        val con = factory.newConnection()
        val channel = con.createChannel()
        channel.queueDeclare("responseQueue", false, false, false, null)
        channel.basicPublish("", "responseQueue", null, "fuck".toByteArray())
        println("kek")
    }