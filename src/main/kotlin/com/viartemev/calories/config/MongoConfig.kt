package com.viartemev.calories.config

import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoClients
import com.viartemev.calories.repostitory.IngestionRepository
import com.viartemev.calories.repostitory.UserRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories

@Configuration
@EnableReactiveMongoRepositories(basePackageClasses = [(IngestionRepository::class), (UserRepository::class)])
class MongoConfig : AbstractReactiveMongoConfiguration() {

    override fun reactiveMongoClient(): MongoClient = mongoClient()

    override fun getDatabaseName() = "calories-calculator"

    @Bean
    override fun reactiveMongoTemplate() = ReactiveMongoTemplate(mongoClient(), databaseName)

    @Bean
    fun mongoClient() = MongoClients.create()

}