package com.viartemev.calories.repostitory

import com.viartemev.calories.model.CaloriesPerDay
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation.*
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux


@Repository
class IngestionRepositoryCustomImpl(val reactiveMongoTemplate: ReactiveMongoTemplate) : IngestionRepositoryCustom {


    override fun aggregateByUserId(userId: Long): Flux<CaloriesPerDay> {
        val criteria = match(Criteria.where("userId").`is`(userId))

        val groupBy = group("year", "month", "day")
                .last("year").`as`("year")
                .last("month").`as`("month")
                .last("day").`as`("day")
                .sum("calories").`as`("total")

        val sortBy = sort(Sort.Direction.ASC, "year", "month", "day")

        val dateProjection = project()
                .and("meal").`as`("meal")
                .and("calories").`as`("calories")
                .and("time").extractYear().`as`("year")
                .and("time").extractMonth().`as`("month")
                .and("time").extractDayOfMonth().`as`("day")

        val aggregation = newAggregation(criteria, dateProjection, groupBy, sortBy)
        return reactiveMongoTemplate
                .aggregate(aggregation, "ingestions", IngestionAggregationResult::class.java)
                .map { CaloriesPerDay(it.year, it.month, it.day, it.total) }
    }

}

private data class IngestionAggregationResult(val year: Int?, val month: Int?, val day: Int?, val total: Int?)