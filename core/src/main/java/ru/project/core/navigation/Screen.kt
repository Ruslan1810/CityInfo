package ru.project.core.navigation


import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import ru.project.domain.model.CityItem

@Serializable
sealed class Screen : NavKey {
    @Serializable
    data object Cities : Screen()

    @Serializable
    data class CityDetails(val cityItem: CityItem) : Screen()
}