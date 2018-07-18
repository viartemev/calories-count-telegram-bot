package com.viartemev.calories.bot

import javax.annotation.PostConstruct

//@Service
class BotRunner(val botService: BotService) {

    @PostConstruct
    fun start() = botService.runBot()

}