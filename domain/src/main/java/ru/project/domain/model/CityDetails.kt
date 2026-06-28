package ru.project.domain.model


data class CityDetails(
    val name: String,
    val country: String,
    val population: Int,
    val timezone: String,
    val coordinates: Pair<Double, Double>,
)