package com.santi.pmdm.virgen.dogapicleanarchitecture.domain.repository

import com.santi.pmdm.virgen.dogapicleanarchitecture.data.datasource.database.entities.DogEntity
import com.santi.pmdm.virgen.dogapicleanarchitecture.domain.models.Dog

interface DogRepositoryInterface {
    fun getDogs() : List<Dog>
    fun getBreedDogs(breed:String) : List<Dog>

    /*
    Todas las operaciones con la BBDD, las haremos asíncronas.
    Por tanto le ponemos suspend.
     */
    suspend fun getDogsEntity() : List<Dog>
    suspend fun getBreedDogsEntity(breed:String) : List<Dog>


    suspend fun insertBreedEntitytoDatabase(listEntity : List<DogEntity>)

    suspend fun deleteDatabase()
    suspend fun deleteDog(dog: Dog)
}