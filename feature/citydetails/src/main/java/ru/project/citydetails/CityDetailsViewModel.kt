package ru.project.citydetails

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import ru.project.core.utils.UrlBuilder
import ru.project.domain.model.CityItem
import javax.inject.Inject

@HiltViewModel
class CityDetailsViewModel @Inject constructor(
) : ContainerHost<CityDetailsState, CityDetailsSideEffect>, ViewModel() {

    override val container = container<CityDetailsState, CityDetailsSideEffect>(CityDetailsState())

    fun loadCity(cityItem: CityItem) = intent {
        reduce {
            state.copy(
                cityItem = cityItem,
                isLoading = false,
                error = null,
            )
        }
    }

    fun onSearchInBrowser() = intent {
        val cityName = state.cityItem?.name ?: return@intent
        val url = UrlBuilder.buildGoogleSearch(cityName)
        postSideEffect(CityDetailsSideEffect.OpenBrowser(url))
    }

    fun retry() = intent {
        if (state.cityItem != null) {
            reduce {
                state.copy(
                    isLoading = false,
                    error = null,
                )
            }
        }
    }
}

