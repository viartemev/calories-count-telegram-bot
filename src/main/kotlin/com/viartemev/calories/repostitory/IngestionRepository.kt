package com.viartemev.calories.repostitory

import com.viartemev.calories.model.Ingestion
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux
import java.time.Instant


interface IngestionRepository : ReactiveMongoRepository<Ingestion, String>, IngestionRepositoryCustom {

    fun findByUserIdAndTimeBetween(userId: Long, from: Instant, to: Instant): Flux<Ingestion>

}