package ru.project.domain.repository

import ru.project.domain.model.CityItem
import ru.project.domain.network.ApiResult

interface CitiesRepository {
    suspend fun getCities(query: String, page: Int, limit: Int): ApiResult<List<CityItem>>
}