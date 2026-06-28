package ru.project.core.navigation

import android.os.Bundle

sealed class NavigationResult {
    data class Success(val data: Bundle = Bundle()) : NavigationResult()
    data object Canceled : NavigationResult()
    data class Error(val throwable: Throwable) : NavigationResult()
}