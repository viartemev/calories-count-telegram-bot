package com.viartemev.calories

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories

@EnableReactiveMongoRepositories
@SpringBootApplication
class CaloriesApplication

fun main(args: Array<String>) {
    runApplication<CaloriesApplication>(*args)
}
