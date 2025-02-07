package com.example.srodenas.example_with_catalogs.domain.alerts.usecase

import com.example.srodenas.example_with_catalogs.domain.alerts.models.Alert
import com.example.srodenas.example_with_catalogs.domain.alerts.models.ListAlerts
import com.example.srodenas.example_with_catalogs.domain.alerts.models.RepositoryAlerts

class UseCaseAddAlert(val repo: RepositoryAlerts) {
    suspend fun add(alert: Alert): Int {
        // Inserta la alerta y obtiene el id generado (Long)
        val newId = repo.addAlertForRepository(alert)
        // Actualiza el objeto de dominio con el id generado
        alert.id = newId.toInt()
        // Añade la alerta a la lista global (caché)
        ListAlerts.list.alerts.add(alert)
        return ListAlerts.list.alerts.lastIndex
    }
}
