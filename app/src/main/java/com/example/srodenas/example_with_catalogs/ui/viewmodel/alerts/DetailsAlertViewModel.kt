package com.example.srodenas.example_with_catalogs.ui.viewmodel.alerts

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.srodenas.example_with_catalogs.domain.alerts.models.Alert



class DetailsAlertViewModel : ViewModel() {
    var alertLiveData = MutableLiveData<Alert>()

}