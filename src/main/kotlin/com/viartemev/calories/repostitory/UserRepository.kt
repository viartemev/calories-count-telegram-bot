package com.viartemev.calories.repostitory

import com.viartemev.calories.model.User
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface UserRepository : ReactiveMongoRepository<User, String>
