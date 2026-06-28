package ru.project.cities.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.project.domain.model.CityItem
import ru.project.domain.network.ApiResult
import ru.project.domain.usecase.GetCitiesUseCase

class CityPagingSource(
    private val getCitiesUseCase: GetCitiesUseCase,
    private val query: String,
) : PagingSource<Int, CityItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CityItem> {
        return try {
            val page = params.key ?: 1
            val result = getCitiesUseCase(
                query = query,
                page = page,
                limit = params.loadSize,
            )

            when (result) {
                is ApiResult.Success -> {
                    LoadResult.Page(
                        data = result.data,
                        prevKey = if (page > 1) page - 1 else null,
                        nextKey = if (result.data.isNotEmpty()) page + 1 else null,
                    )
                }
                is ApiResult.Error -> {
                    LoadResult.Error(result.throwable)
                }
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, CityItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}