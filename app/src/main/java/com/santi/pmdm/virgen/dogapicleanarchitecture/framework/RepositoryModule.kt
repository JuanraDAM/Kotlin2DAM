package com.santi.pmdm.virgen.dogapicleanarchitecture.framework

import com.santi.pmdm.virgen.dogapicleanarchitecture.data.repository.DogRepository
import com.santi.pmdm.virgen.dogapicleanarchitecture.domain.repository.DogRepositoryInterface
import com.santi.pmdm.virgen.dogapicleanarchitecture.data.datasource.mem.service.DogService
import com.santi.pmdm.virgen.dogapicleanarchitecture.data.datasource.database.dao.DogDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideDogRepository(
        dogService: DogService,  // Hilt inyecta DogService ya que tiene @Inject en su constructor
        dogDao: DogDao           // Hilt ya provee DogDao a través de tu RoomModule
    ): DogRepositoryInterface {
        return DogRepository(dogService, dogDao)
    }
}
