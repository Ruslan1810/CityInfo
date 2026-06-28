package ru.project.cities.model

import ru.project.domain.model.CityItem

data class CitiesState(
    val query: String = "",
)

sealed class CitiesSideEffect {
    data class NavigateToCityDetails(val cityItem: CityItem) : CitiesSideEffect()
}