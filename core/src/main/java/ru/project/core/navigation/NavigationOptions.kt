package ru.project.core.navigation

data class NavigationOptions(
    val launchSingleTop: Boolean = true,
    val popUpTo: Destination? = null,
    val inclusive: Boolean = false,
    val animated: Boolean = true,
)