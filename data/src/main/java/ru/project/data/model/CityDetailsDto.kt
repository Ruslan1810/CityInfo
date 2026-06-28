package ru.project.data.model


import kotlinx.serialization.Serializable

@Serializable
data class CityDetailsDto(
    val name: String?,
    val country: String?,
    val population: Int?,
    val timezone: String?,
    val lat: Double?,
    val lon: Double?,
)