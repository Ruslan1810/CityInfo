package ru.project.core.navigation


interface Navigator {
    val currentDestination: Destination
    val backStack: List<Destination>
    val canGoBack: Boolean

    fun navigateTo(
        destination: Destination,
        options: NavigationOptions = NavigationOptions(),
        onResult: ((NavigationResult) -> Unit)? = null,
    )

    fun navigateBack(result: NavigationResult? = null): Boolean

    fun popTo(destination: Destination, inclusive: Boolean = false)

    fun popToRoot()

    fun registerResultListener(
        key: String,
        listener: (NavigationResult) -> Unit,
    )

    fun unregisterResultListener(key: String)
}