package com.santi.pmdm.virgen.dogapicleanarchitecture.domain.usercase

import com.santi.pmdm.virgen.dogapicleanarchitecture.domain.models.Dog
import com.santi.pmdm.virgen.dogapicleanarchitecture.domain.repository.DogRepositoryInterface
import com.santi.pmdm.virgen.dogapicleanarchitecture.domain.mapper.toDogEntity
import javax.inject.Inject

class DeleteDogUseCase @Inject constructor(
    private val dogRepository: DogRepositoryInterface
) {
    /**
     * Elimina el perro indicado. Tras la eliminación, si la base de datos queda vacía,
     * se recargan los perros nativos.
     */
    suspend operator fun invoke(dog: Dog) {
        // Se elimina el perro (convertimos a entidad y eliminamos)
        dogRepository.deleteDog(dog)
        // Verificamos si quedan perros en la base de datos
        val dogsFromDb = dogRepository.getDogsEntity()
        if (dogsFromDb.isEmpty()) {
            // Cargar los perros nativos
            val nativeDogs = dogRepository.getDogs()
            // Convertir a entidad e insertar en la BBDD
            val dataDogEntity = nativeDogs.map { it.toDogEntity() }
            dogRepository.insertBreedEntitytoDatabase(dataDogEntity)
        }
    }
}
