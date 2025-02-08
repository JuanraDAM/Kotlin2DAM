package com.santi.pmdm.virgen.dogapicleanarchitecture.framework

import com.santi.pmdm.virgen.dogapicleanarchitecture.data.datasource.database.dao.DogDao
import com.santi.pmdm.virgen.dogapicleanarchitecture.domain.repository.DogRepositoryInterface
import com.santi.pmdm.virgen.dogapicleanarchitecture.domain.usercase.DeleteDogUseCase
import com.santi.pmdm.virgen.dogapicleanarchitecture.domain.usercase.DeleteDogsFromDataBaseUseCase
import com.santi.pmdm.virgen.dogapicleanarchitecture.domain.usercase.GetDogsBreedUseCase
import com.santi.pmdm.virgen.dogapicleanarchitecture.domain.usercase.GetDogsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideGetDogsUseCase(
        repository: DogRepositoryInterface
    ): GetDogsUseCase = GetDogsUseCase(repository)

    @Provides
    @Singleton
    fun provideGetDogsBreedUseCase(
        repository: DogRepositoryInterface
    ): GetDogsBreedUseCase = GetDogsBreedUseCase(repository)

    @Provides
    @Singleton
    fun provideDeleteDogsFromDataBaseUseCase(
        dogDao: DogDao
    ): DeleteDogsFromDataBaseUseCase = DeleteDogsFromDataBaseUseCase(dogDao)

    @Provides
    @Singleton
    fun provideDeleteDogUseCase(
        repository: DogRepositoryInterface
    ): DeleteDogUseCase = DeleteDogUseCase(repository)
}
