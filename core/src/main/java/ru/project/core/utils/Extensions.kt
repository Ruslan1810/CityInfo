package ru.project.core.utils

import java.util.Locale

fun Int?.orEmpty(): Int = this ?: 0

fun Double?.orEmpty(): Double = this ?: 0.0


fun String.toCountryName(locale: Locale): String {
    return Locale("", this).getDisplayCountry(locale)
}

fun Throwable.toErrorInfo(): Pair<String, String> {

    return when (this) {
        is java.net.UnknownHostException -> {
            "Нет подключения к интернету" to "Проверьте соединение и попробуйте снова. Без интернета данные  не загрузятся"
        }

        is java.net.SocketTimeoutException -> {
            "Сервер не отвечает" to "Попробуйте позже или проверьте соединение"
        }

        is java.net.ConnectException -> {
            "Не удается подключиться" to "Проверьте интернет-соединение"
        }

        else -> {
            "Ошибка" to (message ?: "Что-то пошло не таккккек")
        }
    }
}