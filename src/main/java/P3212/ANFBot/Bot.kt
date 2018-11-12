package P3212.ANFBot

import com.rabbitmq.client.*
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import java.lang.Exception
import java.lang.IllegalStateException
import javax.annotation.Resource

class Bot : TelegramLongPollingBot() {
    private val token = "754140988:AAHSkm4XIO8TXMxvlU49JHJJtYPQprNlUJM"
    private var currentChat: Long = -1
    override fun getBotToken(): String {
        return token
    }


    override fun onUpdateReceived(update: Update) {
        if (update.hasMessage() && update.message.hasText()) {
            currentChat = update.message.chatId
            val list = update.message.text.toLowerCase().split(" ")
            when (list[0]) {
                "/help", "help" -> answer("Available commands are:\n")
                "/top", "top" -> request("top")
                "/events", "events" -> request("events")
                "/stats", "stats" ->
                    if (list.size == 2)
                        request("stats ${list[1]}")
                    else answer("Invalid command arguments")
                else -> answer("Invalid command. Use /help for the commands list.")
            }
        }
    }

    private fun answer(message: String) {
        execute(SendMessage(currentChat, message))

    }

    override fun getBotUsername(): String? {
        return null
    }

    private fun request(message: String) {
        val factory = ConnectionFactory()
        factory.host = "localhost"
        val connection = factory.newConnection()
        val channel = connection.createChannel()
        channel.queueDeclare("requestQueue", false, false, false, null)
        channel.basicPublish("", "requestQueue", null, message.toByteArray())
        response()
    }

    private fun response() {
        val factory = ConnectionFactory()
        factory.host = "localhost"
        val con = factory.newConnection()
        val channel = con.createChannel()
        val consumer = object : DefaultConsumer(channel) {
            override fun handleDelivery(consumerTag: String?, envelope: Envelope?, properties: AMQP.BasicProperties?, body: ByteArray?) {
                answer(String(body ?: ByteArray(0)))
            }
        }
        channel.queueDeclare("responseQueue", false, false, false, null)
        channel.basicConsume("responseQueue", true, consumer)
    }
}
