package P3212.ANFBot

import com.rabbitmq.client.*
import com.rabbitmq.jms.admin.RMQConnectionFactory
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import java.lang.Exception
import java.lang.IllegalStateException
import javax.annotation.Resource
import javax.jms.*

class Bot : TelegramLongPollingBot() {
    private val token = "754140988:AAHSkm4XIO8TXMxvlU49JHJJtYPQprNlUJM"
    private val queueName = "bot"
    override fun getBotToken(): String {
        return token
    }


    override fun onUpdateReceived(update: Update) {
        if (update.hasMessage() && update.message.hasText()) {
            val id = update.message.chatId;
            val list = update.message.text.toLowerCase().split(" ")
            when (list[0]) {
                "/help", "help" -> answer("Available commands are:\n", id)
                "/top", "top" -> request("top", id)
                "/events", "events" -> request("events", id)
                "/stats", "stats" ->
                    if (list.size == 2)
                        request("stats ${list[1]}", id)
                    else answer("Invalid command arguments", id)
                else -> answer("Invalid command. Use /help for the commands list.", id)
            }
        }
    }

    private fun request(msg: String, chatId: Long) {
        val factory = RMQConnectionFactory()
        factory.host = "localhost"
        val con = factory.createConnection()
        val session = con.createSession(false, Session.AUTO_ACKNOWLEDGE)
        val producer = session.createProducer(session.createQueue("bot"))
        con.start()
        producer.send(session.createTextMessage(msg))
        val consumer = session.createConsumer(session.createQueue("botResponse"))
        val message = consumer.receive(3000)
        answer((message as TextMessage).text, chatId)
    }

    private fun getResponse() {

    }


    override fun getBotUsername(): String? {
        return null
    }

    private fun answer(msg: String, chatId: Long) {
        val sendMessage = SendMessage(chatId, msg)
        execute(sendMessage)
    }

}
