package ru.project.data.network.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import ru.project.data.model.CitiesResponseDto
import javax.inject.Inject


class CitiesApi @Inject constructor(
    private val client: HttpClient,
) {

    suspend fun getCities(
        query: String = "",
        page: Int = 1,
        limit: Int = 10,
    ): CitiesResponseDto {
        return client.get("/api/cities") {
            parameter("query", query)
            parameter("page", page)
            parameter("limit", limit)
        }.body()
    }
}