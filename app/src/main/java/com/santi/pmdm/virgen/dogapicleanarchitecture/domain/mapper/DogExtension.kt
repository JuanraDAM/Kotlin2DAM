package com.santi.pmdm.virgen.dogapicleanarchitecture.domain.mapper

import com.santi.pmdm.virgen.dogapicleanarchitecture.data.datasource.database.entities.DogEntity
import com.santi.pmdm.virgen.dogapicleanarchitecture.domain.models.Dog


/*
Funciones de extensión, para convertir objetos de Tipo DogEntity/Pair a Dog
 */


// Convertir una entidad a modelo de dominio (ahora se preserva el id)
fun DogEntity.toDog(): Dog {
    return Dog(
        id = this.id,
        breed = this.breed,
        image = this.image
    )
}

// Convertir un modelo de dominio a entidad.
// Si el id es 0 (valor por defecto), Room generará uno nuevo al insertar.
fun Dog.toDogEntity(): DogEntity {
    return DogEntity(
        id = this.id, // Esto asegura que, si el objeto Dog proviene de la base de datos, se conserva el id.
        breed = this.breed,
        image = this.image
    )
}


//Necesitamos mapear un Pair<String,String> a Dog  Pair --> IU
fun Pair<String, String>.toDog(): Dog {
    return Dog(breed = first, image = second)
}
