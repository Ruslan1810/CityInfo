package ru.project.data.repository

import ru.project.data.mapper.toDomainCities
import ru.project.data.network.api.CitiesApi
import ru.project.domain.network.ApiResult
import ru.project.domain.network.ApiResult.*
import ru.project.domain.repository.CitiesRepository
import ru.project.domain.model.CityItem
import javax.inject.Inject


class CitiesRepositoryImpl @Inject constructor(
    private val api: CitiesApi,
) : CitiesRepository {

    override suspend fun getCities(
        query: String,
        page: Int,
        limit: Int
    ): ApiResult<List<CityItem>> = try {
        val response = api.getCities(query, page, limit)
        val mapped = response.items.toDomainCities()
        Success(mapped)
    } catch (e: Exception) {
        Error(e)
    }
}