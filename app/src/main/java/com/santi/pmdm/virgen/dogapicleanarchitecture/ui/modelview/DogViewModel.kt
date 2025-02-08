package com.santi.pmdm.virgen.dogapicleanarchitecture.ui.modelview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.santi.pmdm.virgen.dogapicleanarchitecture.domain.models.Dog
import com.santi.pmdm.virgen.dogapicleanarchitecture.domain.usercase.DeleteDogsFromDataBaseUseCase
import com.santi.pmdm.virgen.dogapicleanarchitecture.domain.usercase.GetDogsBreedUseCase
import com.santi.pmdm.virgen.dogapicleanarchitecture.domain.usercase.DeleteDogUseCase
import com.santi.pmdm.virgen.dogapicleanarchitecture.domain.usercase.GetDogsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Provider

/*
* Autor: santiago rodenas herráiz
* Email: srodher115@g.educaand.es

viewModelScope.launch, es la forma de crear una corrutinas. Son tareas
que se hacen en segundo plano, cuando requieren de operaciones asíncronas.
Es conveniente utilizarlas, para no bloquear a la aplicación. En este ejemplo,
no es necesario, ya que simulamos un acceso a memoria. Como queremos utilizar un
progressbar por un tiempo, no tengo mas remedio que lanzarlo como si fuera una
operación asíncrona. De esta forma, la operación se duerme durante un tiempo
mientras la interfaz va funcionando por otro lado.

Será totalmente necesarias utilizar las corrutinas, en operaciones de datos
por internet.
 */
/*
Con la anotación siguiente:
1.- Preparamos para que podamos inyectar aquí.
2.- Modificamos el constructor de nuestro DogViewModel con
los objetos que necesitamos.
3.- Necesito un Provider para el segundo caso de uso, porque dicha clase que debe inyectar,
antes de crearse el objeto, necesito parsarle un parámetro. Cada vez que llamamos al método get() dentro
del Provider, se está creando una nueva instancia de GetDogsBreedUseCase. Hilt, creará inyecta
el Proveedor de GetDogsBreedUseCase, y Hilt creará autormáticamente un objeto de tipo GetDogsBreedUseCAse
cuando se llame al método get del provider. De esta forma, aseguramos que se cumple todo el ciclo de vida.
 */
@HiltViewModel
class DogViewModel @Inject constructor(
    private val useCaseList: GetDogsUseCase,
    private val getDogsBreedUseCase: GetDogsBreedUseCase,
    private val userCaseDeleteDatabase: DeleteDogsFromDataBaseUseCase,
    private val deleteDogUseCase: DeleteDogUseCase  // <-- Parámetro nuevo
) : ViewModel() {

    var dogListLiveData = MutableLiveData<List<Dog>>() // repositorio observable
    var progressBarLiveData = MutableLiveData<Boolean>() // progressbar observable
    var breed = MutableLiveData<String>() // para el campo search con la raza.

    fun list() {
        viewModelScope.launch {
            progressBarLiveData.value = true // notifico
            delay(500)
            var data: List<Dog>?
            withContext(Dispatchers.IO) {
                data = useCaseList()  // se invoca y se obtienen los datos.
            }
            data.let {
                dogListLiveData.value = it  // notifico
                progressBarLiveData.value = false  // notifico
            }
        }
    }

    fun listForBreed(breed: String) {
        viewModelScope.launch {
            progressBarLiveData.value = true // notifico
            delay(500)
            var data: List<Dog>?
            withContext(Dispatchers.IO) {
                getDogsBreedUseCase.setBreed(breed)  // setear la raza antes de invocar el caso de uso
                data = getDogsBreedUseCase()  // se obtienen los datos.
            }
            data.let {
                dogListLiveData.value = it  // notifico
                progressBarLiveData.value = false  // notifico
            }
        }
    }

    fun searchByBreed(breed: String) {
        this.breed.value = breed  // notificamos cambio
    }

    fun delete() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                userCaseDeleteDatabase() // elimina todos los datos de la BBDD.
            }
            list() // vuelve a cargar los datos desde Dogs.
        }
    }

    fun deleteDog(dog: Dog) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                // Invoca el caso de uso para eliminar el perro individual
                deleteDogUseCase(dog)
            }
            list() // Actualiza la lista de perros tras la eliminación.
        }
    }
}
