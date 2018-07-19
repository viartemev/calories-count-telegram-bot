package com.viartemev.calories.router

import com.viartemev.calories.handler.UserCaloriesHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.router

@Configuration
class Router {

    @Bean
    fun route(userCaloriesHandler: UserCaloriesHandler) = router {
        GET("/users/{id}/calories", userCaloriesHandler::get)
    }

}