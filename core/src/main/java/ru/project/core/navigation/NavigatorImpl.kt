package ru.project.core.navigation

import android.os.Bundle
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigatorImpl @Inject constructor() : Navigator {

    private val _backStack = mutableStateListOf<Destination>()
    private val _currentDestination = mutableStateOf<Destination?>(null)

    private val resultListeners = mutableMapOf<String, MutableList<(NavigationResult) -> Unit>>()
    private val pendingResults = mutableMapOf<String, NavigationResult>()

    override val currentDestination: Destination
        get() = _currentDestination.value ?: Destination.Cities

    override val backStack: List<Destination>
        get() = _backStack.toList()

    override val canGoBack: Boolean
        get() = _backStack.size > 1

    init {
        // Стартовый экран
        _backStack.add(Destination.Cities)
        _currentDestination.value = Destination.Cities
    }

    override fun navigateTo(
        destination: Destination,
        options: NavigationOptions,
        onResult: ((NavigationResult) -> Unit)?,
    ) {
        // Проверка на дубликат
        if (options.launchSingleTop && _currentDestination.value == destination) {
            onResult?.invoke(NavigationResult.Success(Bundle()))
            return
        }

        // PopUpTo
        options.popUpTo?.let { popDestination ->
            val index = _backStack.indexOfLast { it == popDestination }
            if (index != -1) {
                val newSize = if (options.inclusive) index else index + 1
                _backStack.removeRange(newSize, _backStack.size)
            }
        }

        // Добавляем новый экран
        _backStack.add(destination)
        _currentDestination.value = destination

        // Сохраняем колбек
        onResult?.let {
            val key = destination.route
            resultListeners.getOrPut(key) { mutableListOf() }.add(it)
        }

        // Проверяем ожидающие результаты
        processPendingResults(destination)
    }

    override fun navigateBack(result: NavigationResult?): Boolean {
        if (_backStack.size <= 1) return false

        val previous = _backStack[_backStack.size - 2]
        _backStack.removeLastOrNull()
        _currentDestination.value = previous

        result?.let {
            notifyResultListeners(previous, it)
        }

        return true
    }

    override fun popTo(destination: Destination, inclusive: Boolean) {
        val index = _backStack.indexOfLast { it == destination }
        if (index != -1) {
            val newSize = if (inclusive) index else index + 1
            _backStack.removeRange(newSize, _backStack.size)
            _currentDestination.value = _backStack.lastOrNull()
        }
    }

    override fun popToRoot() {
        _backStack.removeRange(1, _backStack.size)
        _currentDestination.value = _backStack.firstOrNull()
    }

    override fun registerResultListener(
        key: String,
        listener: (NavigationResult) -> Unit,
    ) {
        resultListeners.getOrPut(key) { mutableListOf() }.add(listener)
    }

    override fun unregisterResultListener(key: String) {
        resultListeners.remove(key)
    }

    private fun notifyResultListeners(
        destination: Destination,
        result: NavigationResult,
    ) {
        val key = destination.route
        resultListeners[key]?.forEach { it.invoke(result) }
        resultListeners.remove(key)
    }

    private fun processPendingResults(destination: Destination) {
        val key = destination.route
        pendingResults[key]?.let { result ->
            resultListeners[key]?.forEach { it.invoke(result) }
            resultListeners.remove(key)
            pendingResults.remove(key)
        }
    }
}