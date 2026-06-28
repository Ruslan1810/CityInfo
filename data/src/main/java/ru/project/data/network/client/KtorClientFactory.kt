package ru.project.data.network.client


import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import ru.project.core.constants.AppConfig

object KtorClientFactory {
    fun create(baseUrl: String = AppConfig.BASE_URL): HttpClient {
        return HttpClient(CIO) {

            install(HttpTimeout) {
                connectTimeoutMillis = 10_000
                requestTimeoutMillis = 15_000
                socketTimeoutMillis = 10_000
            }

            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }

            defaultRequest {
                url(baseUrl)
            }

            expectSuccess = false
        }
    }
}