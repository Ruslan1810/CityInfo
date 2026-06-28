package ru.project.core.navigation

import kotlinx.serialization.Serializable
import ru.project.domain.model.CityItem

@Serializable
sealed class Destination(
    open val route: String,
    open val analyticsName: String = this::class.simpleName ?: "unknown",
) {
    @Serializable
    data object Cities : Destination(
        route = "cities",
        analyticsName = "cities_screen"
    )

    @Serializable
    data class CityDetails(
        val cityItem: CityItem,
    ) : Destination(
        route = "city_details/{cityId}",
        analyticsName = "city_details_screen"
    )
}