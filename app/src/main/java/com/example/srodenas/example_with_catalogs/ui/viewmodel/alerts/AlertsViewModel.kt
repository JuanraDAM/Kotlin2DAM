package com.example.srodenas.example_with_catalogs.ui.viewmodel.alerts

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.srodenas.example_with_catalogs.domain.alerts.models.Alert
import com.example.srodenas.example_with_catalogs.domain.alerts.models.ListAlerts
import com.example.srodenas.example_with_catalogs.domain.alerts.models.RepositoryAlerts
import com.example.srodenas.example_with_catalogs.domain.alerts.usecase.UseCaseAddAlert
import com.example.srodenas.example_with_catalogs.domain.alerts.usecase.UseCaseForPosition
import com.example.srodenas.example_with_catalogs.domain.alerts.usecase.UseCaseShowAlerts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AlertsViewModel : ViewModel() {
    var posNewAlertLiveDate = MutableLiveData<Int>() // Notifica la posición del ítem insertado
    var posDeleteAlertLiveData = MutableLiveData<Int>() // Notifica la posición del ítem borrado
    var listAlertsLiveData = MutableLiveData<List<Alert>>()  // Lista completa de alertas para la UI

    private val useCaseShowAlerts = UseCaseShowAlerts(RepositoryAlerts.repo)
    private val useCaseAddAlert = UseCaseAddAlert(RepositoryAlerts.repo)
    private val useCaseForPosition = UseCaseForPosition(RepositoryAlerts.repo)

    fun showAlerts() {
        viewModelScope.launch(Dispatchers.IO) {
            ListAlerts.list.alerts = useCaseShowAlerts.showAlerts() // Carga las alertas desde la BBDD
            withContext(Dispatchers.Main) {
                listAlertsLiveData.value = ListAlerts.list.alerts
            }
        }
    }

    fun addAlerts(newAlert: Alert) {
        viewModelScope.launch(Dispatchers.IO) {
            val pos = useCaseAddAlert.add(newAlert)
            withContext(Dispatchers.Main) {
                posNewAlertLiveDate.value = pos
                listAlertsLiveData.value = ListAlerts.list.alerts
            }
        }
    }

    fun delAlert(pos: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            if (pos in ListAlerts.list.alerts.indices) {
                val alertToDelete = ListAlerts.list.alerts[pos]
                RepositoryAlerts.repo.deleteAlertForRepository(alertToDelete)
                ListAlerts.list.alerts.removeAt(pos)

                withContext(Dispatchers.Main) {
                    posDeleteAlertLiveData.value = pos
                    listAlertsLiveData.value = ListAlerts.list.alerts
                }
            }
        }
    }





    fun getAlertForPosition(pos: Int): Alert = useCaseForPosition.devAlert(pos)
}
