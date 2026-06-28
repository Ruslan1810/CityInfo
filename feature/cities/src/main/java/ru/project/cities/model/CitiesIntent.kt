package ru.project.cities.model

import ru.project.domain.model.CityItem

sealed class CitiesIntent {
    data class SearchQuery(val query: String) : CitiesIntent()
    object LoadMore : CitiesIntent()
    data class CityClicked(val cityItem: CityItem) : CitiesIntent()
}