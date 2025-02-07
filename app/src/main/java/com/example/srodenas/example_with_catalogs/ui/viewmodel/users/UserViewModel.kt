package com.example.srodenas.example_with_catalogs.ui.viewmodel.users

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.srodenas.example_with_catalogs.R
import com.example.srodenas.example_with_catalogs.domain.users.models.Profile
import com.example.srodenas.example_with_catalogs.domain.users.models.User
import com.example.srodenas.example_with_catalogs.domain.users.usecase.UseCaseLogin
import com.example.srodenas.example_with_catalogs.domain.users.usecase.UseCaseRegisterLogin
import com.example.srodenas.example_with_catalogs.repository.IUserRepository
import com.example.srodenas.example_with_catalogs.repository.UsuarioRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserViewModel(application: Application) : AndroidViewModel(application) {
    // Usamos la interfaz para acceder al repositorio
    private val userRepository: IUserRepository = UsuarioRepository()
    private val useCaseLogin = UseCaseLogin(userRepository)
    private val useCaseRegisterLogin = UseCaseRegisterLogin(userRepository)
    val register = MutableLiveData<Boolean>()

    var isLogginPreferencesLiveData = MutableLiveData<Boolean>(false)
    var usersLiveData = MutableLiveData<MutableList<User>>()
    var posNewUserlLiveData = MutableLiveData<Int>()
    var posDeleteHotelLiveDate = MutableLiveData<Int>()

    // Utilizamos el contexto de aplicación proporcionado por AndroidViewModel.
    private val appContext: Context = application.applicationContext

    /**
     * (Método de conveniencia) Inicializa el Profile si se detecta que hay datos de usuario
     * en SharedPreferences. Al usar AndroidViewModel, ya disponemos de un contexto seguro.
     */
    fun initContext(_context: Context) {
        // Aunque se pasa _context, usamos appContext para garantizar la validez.
        val isLoggedInPreferences = isUserLoggedInShared()
        if (isLoggedInPreferences) {
            getUser()?.let { user ->
                Profile.profile.initialize(user)
                isLogginPreferencesLiveData.value = isLoggedInPreferences
            }
        }
    }

    /**
     * Intenta hacer login con email y password.
     */
    fun isLogin(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = useCaseLogin.login(email, password)
            withContext(Dispatchers.Main) {
                if (user != null) {
                    saveUserPreferences(user.id, user.name, user.email)
                    Profile.profile.initialize(user)
                    isLogginPreferencesLiveData.value = true
                } else {
                    isLogginPreferencesLiveData.value = false
                }
            }
        }
    }

    /**
     * Comprueba si existen las preferencias del usuario.
     */
    private fun isUserLoggedInShared(): Boolean {
        val sharedPreferences = appContext.getSharedPreferences(
            appContext.getString(R.string.pref_user_file),
            Context.MODE_PRIVATE
        )
        val id = sharedPreferences.getInt(appContext.getString(R.string.pref_user_id), -1)
        val name = sharedPreferences.getString(appContext.getString(R.string.pref_user_name), null)
        val email = sharedPreferences.getString(appContext.getString(R.string.pref_user_email), null)
        return (id != -1 && name != null && email != null)
    }

    /**
     * Guarda las preferencias del usuario.
     */
    private fun saveUserPreferences(id: Int, name: String, email: String) {
        val sharedPreferences = appContext.getSharedPreferences(
            appContext.getString(R.string.pref_user_file),
            Context.MODE_PRIVATE
        )
        val editor = sharedPreferences.edit()
        editor.putInt(appContext.getString(R.string.pref_user_id), id)
        editor.putString(appContext.getString(R.string.pref_user_name), name)
        editor.putString(appContext.getString(R.string.pref_user_email), email)
        editor.apply()
    }

    /**
     * Recupera el usuario de las preferencias.
     */
    fun getUser(): User? {
        val sharedPreferences = appContext.getSharedPreferences(
            appContext.getString(R.string.pref_user_file),
            Context.MODE_PRIVATE
        )
        val id = sharedPreferences.getInt(appContext.getString(R.string.pref_user_id), -1)
        if (id == -1) return null

        val name = sharedPreferences.getString(appContext.getString(R.string.pref_user_name), null)
        val email = sharedPreferences.getString(appContext.getString(R.string.pref_user_email), null)
        if (name == null || email == null) return null

        // Retornamos el usuario; los demás campos quedan vacíos si no se han guardado.
        return User(id, name, email, "", "", "")
    }

    /**
     * Registra un usuario utilizando el caso de uso correspondiente.
     */
    fun register(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            val isReg = useCaseRegisterLogin.register(user)
            withContext(Dispatchers.Main) {
                register.value = isReg
            }
        }
    }

    /**
     * Método de logout: limpia las preferencias y resetea el Profile.
     */
    fun logout(context: Context) {
        val sharedPreferences = context.getSharedPreferences(
            context.getString(R.string.pref_user_file), Context.MODE_PRIVATE
        )
        sharedPreferences.edit().clear().apply()
        Profile.profile.reset()
    }

    /**
     * Carga todos los usuarios desde el repositorio y actualiza el LiveData.
     */
    fun showUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            val data: List<User> = userRepository.getAllUsers()
            withContext(Dispatchers.Main) {
                usersLiveData.value = data.toMutableList()
            }
        }
    }

    /**
     * Método para eliminar un usuario. Se pasa el objeto User y se llama al repositorio.
     */
    fun deleteUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = userRepository.eliminarUsuarioPorId(user.id)
            if (result) {
                withContext(Dispatchers.Main) {
                    // Actualizamos la lista de usuarios tras la eliminación.
                    showUsers()
                }
            }
        }
    }

    /**
     * Método para actualizar el nombre de un usuario.
     * Se invoca con el id del usuario y el nuevo nombre.
     */
    fun updateUserName(id: Int, newName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = userRepository.actualizarNombre(id, newName)
            withContext(Dispatchers.Main) {
                if (result) {
                    // Si el usuario actualizado es el logueado, actualizamos también las SharedPreferences y el Profile.
                    val user = getUser()
                    if (user != null && user.id == id) {
                        saveUserPreferences(id, newName, user.email)
                        // Re-inicializamos el Profile con el usuario modificado.
                        Profile.profile.initialize(User(id, newName, user.email, "", "", ""))
                    }
                    // Refrescamos el listado de usuarios.
                    showUsers()
                }
            }
        }
    }
}
