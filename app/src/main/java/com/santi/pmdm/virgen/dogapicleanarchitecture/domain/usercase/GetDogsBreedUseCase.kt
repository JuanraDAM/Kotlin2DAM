package com.santi.pmdm.virgen.dogapicleanarchitecture.domain.usercase

import com.santi.pmdm.virgen.dogapicleanarchitecture.data.datasource.database.entities.DogEntity
import com.santi.pmdm.virgen.dogapicleanarchitecture.domain.repository.DogRepositoryInterface
import com.santi.pmdm.virgen.dogapicleanarchitecture.domain.mapper.toDogEntity
import com.santi.pmdm.virgen.dogapicleanarchitecture.domain.models.Dog
import com.santi.pmdm.virgen.dogapicleanarchitecture.domain.models.Repository
import javax.inject.Inject

/*
Con @Inyect constructor(), estamos diciendo que esa clase ya se puede inyectar.
Necesito la raza.
 */

class GetDogsBreedUseCase @Inject constructor(
    private val dogRepository: DogRepositoryInterface
) {
    private var breed: String = ""
    fun setBreed(breed: String) {
        this.breed = breed
    }

    suspend operator fun invoke(): List<Dog> {
        Repository.dogs = dogRepository.getBreedDogsEntity(breed)
        if (Repository.dogs.isEmpty()) {
            Repository.dogs = dogRepository.getBreedDogs(breed)
            val dataDogEntity: List<DogEntity> = Repository.dogs.map { it.toDogEntity() }
            dogRepository.insertBreedEntitytoDatabase(dataDogEntity)
        }
        return Repository.dogs
    }
}


