package com.santi.pmdm.virgen.dogapicleanarchitecture.data.datasource.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.santi.pmdm.virgen.dogapicleanarchitecture.data.datasource.database.entities.DogEntity

/*
Lo chulo de Room, es que a nivel de App, nosotros trabajamos con objetos, no con los recordSet que nos puede proporcionar
una consulta a una base de datos. Se puede decir que room nos abstrae de realizar la adaptación a objetos.
*/

/*
1.- Con @Query, especificamos una consulta SQL de datos y le damos el nombre de la función que queramos. Si nos damos
cuenta, devolverá una lista de Dog.
2.- Con la segunda @Query, especificamos una consulta por breed.
3.- Con @Insert, no tengo que generar el código sql para realizar la inserción. Necesito declararla como suspend
ya que irá en una corrutina. Permite que se ejecute en segundo plano y no bloquea al hilo principal. Con vararg dogs, queremos
indicar que puede aceptar no sólo un Dog, sino varios Dog. Por ejemplo:
    val dog1 = Dog(breed = "Labrador", imageUrl = "url1")
    val dog2 = Dog(breed = "Beagle", imageUrl = "url2")
    dogDao.insertAll(dog1, dog2)
    En caso de conficto, donde queramos insertar el mismo Dog porque tienen mismo ID, lo que le decimos es que lo Reemplace.
 */
/*
@author santiago rodenas herráiz
@Email srodher115@g.educaand.es
*/

@Dao
interface DogDao {

    // Métodos existentes...
    @Query("SELECT * FROM dogentity")
    suspend fun getAll(): List<DogEntity>

    @Query("SELECT * FROM dogentity WHERE breed = :breed")
    suspend fun getDogsByBreed(breed: String): List<DogEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDog(vararg dogs: DogEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllDog(dogs: List<DogEntity>)

    @Query("DELETE FROM dogentity")
    suspend fun deleteAll()

    // NUEVO: Eliminar un solo perro.
    @Delete
    suspend fun deleteDog(dog: DogEntity)
}
