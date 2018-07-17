package com.viartemev.calories.repostitory

import com.viartemev.calories.model.Ingestion
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux
import java.time.Instant


interface IngestionRepository : ReactiveMongoRepository<Ingestion, String> {

    @Query(value = "{'time':{ \$gte: ?0, \$lt: ?1}, userId: ?2}")
    fun findByDateBetweenForUser(from: Instant, to: Instant, userId: Long): Flux<Ingestion>

}