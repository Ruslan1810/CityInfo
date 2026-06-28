package ru.project.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CityItemDto(
    val id: Int?,
    val name: String?,
    val country: String?,
    val lat: Double?,
    val lon: Double?,
    val pop: Int?,
)