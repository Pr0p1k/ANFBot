package P3212.ANFBot

import org.telegram.telegrambots.ApiContextInitializer
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException

object BotApplication {
    @JvmStatic
    fun main(args: Array<String>) {
        ApiContextInitializer.init()

        val api = TelegramBotsApi()

        try {
            api.registerBot(Bot())
        } catch (e: TelegramApiRequestException) {
            e.printStackTrace()
            System.exit(228)
        }

        println("Bot started.")
    }
}
