package com.viartemev.calories.router

import com.viartemev.calories.handler.IngestionHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.router

@Configuration
class Router {

    @Bean
    fun route(ingestionHandler: IngestionHandler) = router {
        GET("/users/{id}/calories", ingestionHandler::get)
    }

}