package ru.project.cityinfo

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import dagger.hilt.android.AndroidEntryPoint
import ru.project.cities.CitiesScreen
import ru.project.citydetails.CityDetailsScreen
import ru.project.cityinfo.ui.theme.CityInfoTheme
import ru.project.core.navigation.Destination
import ru.project.core.navigation.Navigator
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigator: Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CityInfoTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppNavigation(navigator = navigator)
                }
            }
        }
    }
}


@SuppressLint("UnusedContentLambdaTargetStateParameter")
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavigation(
    navigator: Navigator,
) {
    val backStack by remember { derivedStateOf { navigator.backStack } }
    val currentDestination = backStack.lastOrNull() ?: Destination.Cities

    AnimatedContent(
        targetState = currentDestination,
        transitionSpec = {
            val isForward = backStack.size > 1

            if (isForward) {
                slideInHorizontally(
                                initialOffsetX = { fullWidth -> fullWidth },
                                animationSpec = tween(300)
                            ).togetherWith(
                    slideOutHorizontally(
                        targetOffsetX = { fullWidth -> -fullWidth },
                        animationSpec = tween(300)
                    )
                )
            } else {
                slideInHorizontally(
                                initialOffsetX = { fullWidth -> -fullWidth },
                                animationSpec = tween(300)
                            ).togetherWith(
                    slideOutHorizontally(
                        targetOffsetX = { fullWidth -> fullWidth },
                        animationSpec = tween(300)
                    )
                )
            }
        },
        contentKey = { it.route }
    ) {
        NavDisplay(
            backStack = backStack,
            onBack = { navigator.navigateBack() }
        ) { route ->
            NavEntry(route) {
                when (route) {
                    is Destination.Cities -> {
                        CitiesScreen(
                            onCityClick = { city ->
                                navigator.navigateTo(
                                    Destination.CityDetails(cityItem = city)
                                )
                            }
                        )
                    }

                    is Destination.CityDetails -> {
                        CityDetailsScreen(
                            cityItem = route.cityItem,
                            onBack = { navigator.navigateBack() }
                        )
                    }
                }
            }
        }
    }
}