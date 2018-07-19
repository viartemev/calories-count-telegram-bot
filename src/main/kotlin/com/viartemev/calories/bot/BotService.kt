package com.viartemev.calories.bot

import com.viartemev.calories.config.BotConfig
import com.viartemev.calories.model.Ingestion
import com.viartemev.calories.model.User
import com.viartemev.calories.repostitory.IngestionRepository
import com.viartemev.calories.repostitory.UserRepository
import me.ivmg.telegram.Bot
import me.ivmg.telegram.bot
import me.ivmg.telegram.dispatch
import me.ivmg.telegram.dispatcher.command
import me.ivmg.telegram.entities.Message
import me.ivmg.telegram.entities.Update
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.time.Duration
import java.time.LocalDate
import java.time.ZoneOffset

@Component
class BotService(
        val ingestionRepository: IngestionRepository,
        val userRepository: UserRepository,
        val botConfig: BotConfig
) {

    companion object {
        const val caloriesPerDay = 2000
    }

    @Async
    fun runBot() {
        bot {
            token = botConfig.token
            timeout = 30
            logLevel = botConfig.logLevel
            dispatch {
                command("add", this@BotService::addMeal)
                command("eat_it", this@BotService::canIEatIt)
                command("balance", this@BotService::calculateBalance)
                command("register", this@BotService::registerUser)
            }
        }.startPolling()
    }

    private fun registerUser(bot: Bot, update: Update) {
        val chatId = update.message?.chat?.id ?: return
        val user = update.message?.from
        user?.id ?: return

        Mono.just(user)
                .subscribe {
                    userRepository
                            .save(User(fistName = it.firstName, lastName = it.lastName, userId = it.id))
                            .doOnError { bot.sendMessage(chatId = chatId, text = "Can't register you") }
                            .subscribe { bot.sendMessage(chatId = chatId, text = "You are registered! ") }
                }
    }

    fun addMeal(bot: Bot, update: Update) {
        val chatId = update.message?.chat?.id ?: return
        val userId = update.message?.from?.id ?: return
        Mono.justOrEmpty(update.message)
                .map { extractAddCaloriesArgumentFromMessage(it) }
                .doOnError { e -> bot.sendMessage(chatId = chatId, text = "Error: ${e.message}") }
                .subscribe { (meal, calories) ->
                    ingestionRepository
                            .save(Ingestion(userId = userId, meal = meal, calories = calories))
                            .doOnError { bot.sendMessage(chatId = chatId, text = "Can't save the meal") }
                            .subscribe { bot.sendMessage(chatId = chatId, text = "Meal: ${it.meal} with calories: ${it.calories} was added") }
                }
    }

    fun canIEatIt(bot: Bot, update: Update) {
        val chatId = update.message?.chat?.id ?: return
        val userId = update.message?.from?.id ?: return
        Mono.justOrEmpty(update.message)
                .map { extractCanIEatCaloriesArgumentFromMessage(it) }
                .doOnError { e -> bot.sendMessage(chatId = chatId, text = "Error: ${e.message}") }
                .zipWith(fetchDayBalanceForUser(userId)) { calories, balance -> caloriesPerDay - calories - balance }
                .map { if (it > 0) "Yes, you can eat it" else "No, you can't" }
                .subscribe { bot.sendMessage(chatId = chatId, text = it) }
    }

    fun calculateBalance(bot: Bot, update: Update) {
        val chatId = update.message?.chat?.id ?: return
        val userId = update.message?.from?.id ?: return
        fetchDayBalanceForUser(userId)
                .subscribe { bot.sendMessage(chatId = chatId, text = "You have eaten $it calories!") }
    }

    fun fetchDayBalanceForUser(userId: Long): Mono<Int> {
        val today = LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC)
        val tomorrow = today.plus(Duration.ofDays(1))
        return ingestionRepository
                .findByDateBetweenForUser(today, tomorrow, userId)
                .reduce(0) { acc: Int, ingestion: Ingestion -> acc + ingestion.calories }
    }

    private fun extractCanIEatCaloriesArgumentFromMessage(it: Message) = it.text
            ?.split(" ")
            ?.drop(1)
            ?.let {
                if (it.size != 1) throw RuntimeException("Invalid count of arguments")
                it[0].toIntOrNull() ?: throw RuntimeException("Calories count can't be null")
            } ?: throw RuntimeException("Text can't be empty")

    private fun extractAddCaloriesArgumentFromMessage(it: Message): Pair<String, Int> = it.text
            ?.substringAfter(" ")
            ?.let { listOf(it.substringBeforeLast(" "), it.substringAfterLast(" ")) }
            ?.let {
                if (it.size != 2) throw RuntimeException("Invalid count of arguments")
                Pair(it[0], it[1].toIntOrNull() ?: throw RuntimeException("Calories count can't be null"))
            } ?: throw RuntimeException("Text can't be empty")
}