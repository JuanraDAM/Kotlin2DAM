package com.example.srodenas.example_with_catalogs.ui.viewmodel.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Perfil del usuario registrado"
    }
    val text : LiveData<String> = _text

}