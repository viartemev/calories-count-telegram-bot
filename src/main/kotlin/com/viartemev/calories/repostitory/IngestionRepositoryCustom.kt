package com.viartemev.calories.repostitory

import com.viartemev.calories.model.CaloriesPerDay
import reactor.core.publisher.Flux

interface IngestionRepositoryCustom {
    fun aggregateByUserId(userId: Long): Flux<CaloriesPerDay>
}