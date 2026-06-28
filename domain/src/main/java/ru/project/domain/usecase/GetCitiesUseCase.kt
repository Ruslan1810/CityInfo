package ru.project.domain.usecase

import ru.project.domain.model.CityItem
import ru.project.domain.network.ApiResult
import ru.project.domain.network.INetworkMonitor
import ru.project.domain.repository.CitiesRepository
import java.net.UnknownHostException

class GetCitiesUseCase(
    private val repository: CitiesRepository,
    private val networkMonitor: INetworkMonitor,
) {
    suspend operator fun invoke(
        query: String = "",
        page: Int = 1,
        limit: Int = 20
    ): ApiResult<List<CityItem>> {
        if (!networkMonitor.isConnected()) {
            return ApiResult.Error(
                throwable = UnknownHostException(),
            )
        }
        return repository.getCities(query, page, limit)
    }
}