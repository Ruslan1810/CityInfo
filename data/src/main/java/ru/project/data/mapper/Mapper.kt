package ru.project.data.mapper

import android.util.Log
import ru.project.core.utils.orEmpty
import ru.project.core.utils.toCountryName
import ru.project.data.model.CityDetailsDto
import ru.project.data.model.CityItemDto
import ru.project.domain.model.CityItem
import ru.project.domain.model.CityDetails
import java.util.Locale
import kotlin.text.orEmpty


fun List<CityItemDto?>?.toDomainCities(): List<CityItem> {
    return this?.mapNotNull { it?.toDomainCity() } ?: emptyList()
}

internal fun CityItemDto.toDomainCity(): CityItem {
    return CityItem(
        name = this.name.orEmpty(),
        country = this.country.orEmpty().toCountryName(Locale("ru")),
        pop = this.pop.orEmpty(),
        id = this.id.orEmpty()
    )
}

private fun CityDetailsDto.toCoordinates(): Pair<Double, Double> {
    val lat = this.lat.orEmpty()
    val lon = this.lon.orEmpty()
    return lat to lon
}