package com.viartemev.calories.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document(collection = "ingestions")
data class Ingestion(
        @Id var id: String? = null,
        var userId: Long,
        var meal: String,
        var calories: Int,
        var time: Instant = Instant.now()
)

