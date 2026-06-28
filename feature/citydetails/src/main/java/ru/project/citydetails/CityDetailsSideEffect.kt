package ru.project.citydetails

sealed class CityDetailsSideEffect {
    data class OpenBrowser(val url: String) : CityDetailsSideEffect()
    object GoBack : CityDetailsSideEffect()
}