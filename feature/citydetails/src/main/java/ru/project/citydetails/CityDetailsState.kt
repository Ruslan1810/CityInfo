package ru.project.citydetails

import ru.project.domain.model.CityItem

data class CityDetailsState(
    val cityItem: CityItem? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)