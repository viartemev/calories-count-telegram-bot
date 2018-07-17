package com.viartemev.calories.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "users")
data class User (
        @Id var id: String? = null,
        var fistName: String,
        var lastName: String?,
        var userId: Long
)