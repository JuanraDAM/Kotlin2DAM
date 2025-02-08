package com.santi.pmdm.virgen.dogapicleanarchitecture.domain.models


/*
Este será nuestro modelo en el dominio. Este modelo es totalmente independiente a los dos modos de
acceso a datos.
 */
data class Dog(
    val id: Int = 0, // Valor por defecto 0 para los casos en que no venga de la base de datos.
    val breed: String,
    val image: String
)
