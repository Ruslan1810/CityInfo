package ru.project.cityinfo.di

import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.project.domain.network.INetworkMonitor
import ru.project.domain.repository.CitiesRepository
import ru.project.domain.usecase.GetCitiesUseCase
import javax.inject.Singleton
import dagger.Module

@Module
@InstallIn(SingletonComponent::class)
object UseCasesModule {

    @Provides
    @Singleton
    fun provideGetCitiesUseCase(
        repository: CitiesRepository,
        networkMonitor: INetworkMonitor,
    ): GetCitiesUseCase {
        return GetCitiesUseCase(repository, networkMonitor)
    }
}