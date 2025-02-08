package com.santi.pmdm.virgen.dogapicleanarchitecture.ui.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.pmdm.virgen.dogapi.ui.adapter.DogAdapter
import com.santi.pmdm.virgen.dogapicleanarchitecture.R
import com.santi.pmdm.virgen.dogapicleanarchitecture.databinding.ActivityMainBinding
import com.santi.pmdm.virgen.dogapicleanarchitecture.ui.modelview.DogViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

/*
@author santiago rodenas herráiz
@Email srodher115@g.educaand.es

La idea de esta versión es la siguiente:
1.- Tenemos dos acceso a datos (Uno en Dogs, simulando por ejemplo alguna api, y
una base de datos sqllite al que trabajaremos con Room.
2.- Inicialmente, la aplicación cargará todos los datos de la Base de datos. Pueden pasar dos cosas
 - Que no haya nada en la Base de datos, por tanto tendremos que realizar el otro Acceso a Datos y
 después cargarlo en la Base de datos.
 - Que hayan datos en la Base de datos y por tanto tiraremos sólo del acceso a la base de datos.

3.- Tenemos un botón de eliminar la Base de datos, que lo que hace es sencillo. Se carga la BBDD y vuelve
a cargar los datos del segundo acceso a datos Dogs. En ese proceso, vuelve a insertar todos los datos
en la base de datos.

4.- Tenemos 3 casos de uso:
  Caso de uso que realiza el listado de todos los perros.
  Caso de uso que realiza el listado de todos los perros según una raza.
  Caso de uso que elimina los datos de la base de datos.

5.- Todas las operaciones con el dao, deben ser asíncronas, por tanto suspend.

6.- Necesitamos inyección de dependencias y por tanto lo inyectamos todo.

7.- Necesitamos una instancia de Room y un objeto con la base de datos. Para ello, la inyección
la realizamos dentro de un módulo. Necesitamos que sea objeto único y lo ponemos como singleton

8.- Necesitamos un mapper, que nos independize nuestro modelo del dominio, con el modelo de los datos.
Hay que tener en cuenta, que en datos tenemos dos modelos. Dog (para nuestro primer acceso a datos) y DogEntity
(para nuestro acceso a datos a la database). Puesto que el dominio (lógica de negocio con los casos de uso),
deben ser completamente independiente del acceso a datos, hemos utilizado un fichero DogExtension, donde
ponemos un mapper de DogEntity a DogModel (cuando hay que recuperar los datos de la BBDD), de Dog a DogModel
(cuando hay que recuperar los datos de Dogs) y al insertar los datos en la BBDD, también necesitamos un mapper
de DogModel a DogEntity.

9.- Recordamos que para Room, es necesario cuatro cosas (Interfaz con las operaciones Dao), (instancia de Room),
(instancia a la database)  y nuestro modelo DogEntity (que define los datos y lo relaciona con la tabla y atributos de la BBDD)
Como recuerdo, Room nos implementa todo método abstracto que definimos en la interfaz de acceso a datos.

10.- Nuestro ViewModel, queda prácticamente igual que en la versión anterior (rama de room).
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    //@Inject lateinit var testApi: TestApi
    lateinit var binding: ActivityMainBinding
    lateinit var adapter: DogAdapter
    val dogViewModel: DogViewModel by viewModels() //tiene que ser constante.


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.mySearch.setOnQueryTextListener(this)  //cargamos el listener
        registerLiveData()  //Observamos cambios.
        loadDada() //se cargan todos los datos.
        initRecyclerView()  //inicializamos el recyclerView.
        initEvent()
        //test()
    }

    private fun initEvent() {
        binding.btnDelete.setOnClickListener{
            dogViewModel.delete()
        }
    }

    private fun loadDada() {
        dogViewModel.list()  //simulamos un evento para iniciar la carga de datos desde el viewmodel

    }

    /*
    Quiero suscribir al activity, cuando los datos de dogListLiveData,
    cambién. En el momento que haya ese cambio, el ViewModel notificará
    al activity y ejecutará la lambda.
     */
    private fun registerLiveData() {
        dogViewModel.dogListLiveData.observe(
            this, {  myList->
                //Aquí hacemos la actualización del adapter.
                adapter.dogRepository = myList!!  //aseguro los datos.
                binding.myRecyclerPpal.adapter = adapter  //le asigno el adapter.
                adapter.notifyDataSetChanged()  //No hace falta, pero por si acaso.
            })

        /*
        Cuando exista un cambio en el modelo, quiero que el Activity
        sea notificado para que actualize la ui.
         */
        dogViewModel.progressBarLiveData.observe(
            this, { visible ->
                binding.progressBar.isVisible = visible
                Log.i("TAG-DOGS","ProgressBar esta $visible")            }
        )

        /*
        Observamos un cambio en el search.
         */
        dogViewModel.breed.observe(  //el campo search, ha cambiado
            this, { bread->
                dogViewModel.listForBreed(bread)  //cambiamos los datos.
                hideKeyBoard()

            }
        )
    }

    /*
    Pone todo en funcionamiento
     */
    private fun initRecyclerView(){
        binding.myRecyclerPpal.layoutManager = LinearLayoutManager(this)
        adapter = DogAdapter { dog ->
            // Invocar la acción de eliminación en el ViewModel para el perro seleccionado.
            dogViewModel.deleteDog(dog)
        }
    }



    /*
    Este método, es llamado cuando se escribe algo en el campo y se pulsa.
     */
    override fun onQueryTextSubmit(query: String?): Boolean {

        if (!query.isNullOrEmpty())
            dogViewModel.searchByBreed(query!!)
        return true
    }

    /*
    Cualquier cambio, llamará a este método. Estoy obligado a ponerlo
    Principalmente, lo utilizo para cargar toda la lista de nuevo, al
    estar el campo vacío.
     */
    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText.isNullOrEmpty()) {
            dogViewModel.list()
            hideKeyBoard()
        }
        return true
    }



/*
Método que cierra el teclado. MUY INTERESANTE...
 */
    private fun hideKeyBoard() {
        val imn = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imn.hideSoftInputFromWindow(binding.myLayoutPpal.windowToken, 0)
    }

   /* private fun test() {
        lifecycleScope.launch{
            testApi.testDogApi()
        }

    }

    */
}