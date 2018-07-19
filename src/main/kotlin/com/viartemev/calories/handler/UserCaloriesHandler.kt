package com.viartemev.calories.handler

import com.viartemev.calories.model.CaloriesPerDay
import com.viartemev.calories.repostitory.IngestionRepository
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse

@Component
class UserCaloriesHandler(val ingestionRepository: IngestionRepository) {

    fun get(request: ServerRequest) =
            ServerResponse
                    .ok()
                    .contentType(APPLICATION_JSON)
                    .body(ingestionRepository.aggregateByUserId(request.pathVariable("id").toLong()), CaloriesPerDay::class.java)
}