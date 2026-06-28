package ru.project.cities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import ru.project.cities.model.CitiesSideEffect
import ru.project.cities.model.CitiesState
import ru.project.cities.paging.CityPagingSource
import ru.project.domain.model.CityItem
import ru.project.domain.usecase.GetCitiesUseCase
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class CitiesViewModel @Inject constructor(
    private val getCitiesUseCase: GetCitiesUseCase,
) : ContainerHost<CitiesState, CitiesSideEffect>, ViewModel() {

    override val container = container<CitiesState, CitiesSideEffect>(CitiesState())

    private val searchQueryFlow = MutableSharedFlow<String>()
    private val _pagingDataFlow = MutableStateFlow<PagingData<CityItem>>(PagingData.empty())
    val pagingDataFlow: StateFlow<PagingData<CityItem>> = _pagingDataFlow.asStateFlow()

    init {
        viewModelScope.launch {
            searchQueryFlow
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    intent {
                        reduce { state.copy(query = query) }
                    }
                    getPagingFlow(query)
                }
                .collect { pagingData ->
                    _pagingDataFlow.value = pagingData
                }
        }
    }

    fun loadInitial() {
        viewModelScope.launch {
            searchQueryFlow.emit("")
        }
    }

    fun onQueryChanged(query: String) {
        viewModelScope.launch {
            searchQueryFlow.emit(query)
        }
    }

    fun onCityClicked(cityItem: CityItem) = intent {
        postSideEffect(CitiesSideEffect.NavigateToCityDetails(cityItem))
    }

    fun retry() = intent {
        val currentQuery = state.query
        searchQueryFlow.emit(currentQuery)
    }

    private fun getPagingFlow(query: String): Flow<PagingData<CityItem>> {
        val pagingSource = CityPagingSource(getCitiesUseCase, query)
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                prefetchDistance = 3,
                initialLoadSize = 10,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = { pagingSource },
        ).flow
            .cachedIn(viewModelScope)
    }
}