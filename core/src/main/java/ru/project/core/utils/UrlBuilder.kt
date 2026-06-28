package ru.project.core.utils

object UrlBuilder {

    private const val GOOGLE_SEARCH_BASE = "https://www.google.com/search"
    private const val YANDEX_SEARCH_BASE = "https://yandex.ru/search/"

    fun buildGoogleSearch(query: String): String {
        return "$GOOGLE_SEARCH_BASE?q=${query}"
    }

    fun buildYandexSearch(query: String): String {
        return "$YANDEX_SEARCH_BASE?text=${query}"
    }
}