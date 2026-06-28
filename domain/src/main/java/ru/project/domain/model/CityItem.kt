package ru.project.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class CityItem(
    val name: String,
    val country: String,
    val pop: Int,
    val id: Int,
)