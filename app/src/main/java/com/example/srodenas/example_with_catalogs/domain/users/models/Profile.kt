package com.example.srodenas.example_with_catalogs.domain.users.models

class Profile private constructor() {

    // Ahora user es nullable para poder “limpiar” el perfil al logout.
    var user: User? = null
        private set

    /**
     * Inicializa el Profile con el usuario autenticado.
     */
    fun initialize(user: User) {
        this.user = user
    }

    /**
     * Resetea el Profile para que no tenga usuario.
     */
    fun reset() {
        user = null
    }

    /**
     * Retorna true si la propiedad 'user' no es nula.
     */
    fun isUserInitialized(): Boolean {
        return user != null
    }

    companion object {
        val profile: Profile by lazy {
            Profile()  // Creación del objeto singleton.
        }
    }
}