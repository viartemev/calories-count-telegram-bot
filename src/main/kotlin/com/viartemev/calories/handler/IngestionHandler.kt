package com.viartemev.calories.handler

import com.viartemev.calories.model.IngestionAggregationResult
import com.viartemev.calories.repostitory.IngestionRepository
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters.fromPublisher
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse.ok

@Component
class IngestionHandler(val ingestionRepository: IngestionRepository) {

    fun get(request: ServerRequest) = run {
        //FIXME add validation
        val userId = request.pathVariable("id").toLong()
        val aggregate = ingestionRepository
                .aggregateByUserId(userId)
        //FIXME fix this shit
        aggregate
                .collectList()
                //FIXME add wrapper
                .then(ok().body(fromPublisher(aggregate, IngestionAggregationResult::class.java)))
    }
}