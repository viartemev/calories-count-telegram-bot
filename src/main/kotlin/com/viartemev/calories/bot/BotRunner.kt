package com.viartemev.calories.bot

import org.springframework.stereotype.Service
import javax.annotation.PostConstruct

@Service
class BotRunner(val botService: BotService) {

    @PostConstruct
    fun start() = botService.runBot()

}