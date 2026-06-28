package ru.project.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CitiesResponseDto(
    val items: List<CityItemDto?>?,
    val limit: Int?,
    val page: Int?,
    val total: Int?,
)