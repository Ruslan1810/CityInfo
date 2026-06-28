package ru.project.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import ru.project.data.network.AndroidNetworkMonitor
import ru.project.data.network.api.CitiesApi
import ru.project.data.network.client.KtorClientFactory
import ru.project.domain.network.INetworkMonitor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun provideHttpClient(): HttpClient = KtorClientFactory.create()

    @Singleton
    @Provides
    fun provideCitiesApi(
        client: HttpClient
    ): CitiesApi = CitiesApi(client)

    @Singleton
    @Provides
    fun provideNetworkMonitor(monitor: AndroidNetworkMonitor): INetworkMonitor = monitor

}