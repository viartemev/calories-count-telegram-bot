package com.viartemev.calories.config

import okhttp3.logging.HttpLoggingInterceptor
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "bot")
class BotConfig {
    lateinit var token: String
    lateinit var logLevel: HttpLoggingInterceptor.Level
}