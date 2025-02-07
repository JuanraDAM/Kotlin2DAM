package com.example.srodenas.example_with_catalogs.domain.alerts.usecase

import com.example.srodenas.example_with_catalogs.domain.alerts.models.Alert
import com.example.srodenas.example_with_catalogs.domain.alerts.models.ListAlerts
import com.example.srodenas.example_with_catalogs.domain.alerts.models.RepositoryAlerts

class UseCaseAddAlert(val repo: RepositoryAlerts) {
    suspend fun add(alert: Alert): Int {
        val newId = repo.addAlertForRepository(alert)
        alert.id = newId.toInt()
        ListAlerts.list.alerts.add(alert)
        return ListAlerts.list.alerts.lastIndex
    }
}
