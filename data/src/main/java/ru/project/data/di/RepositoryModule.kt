package ru.project.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.project.data.repository.CitiesRepositoryImpl
import ru.project.domain.repository.CitiesRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCitiesRepository(
        impl: CitiesRepositoryImpl
    ): CitiesRepository





//    @Singleton
//    @Provides
//    fun provideCityDetailsRepository(
//        api: CitiesApi,
//    ) = CityDetailsRepositoryImpl(api)
}