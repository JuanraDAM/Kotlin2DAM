package com.example.srodenas.example_with_catalogs.domain.alerts.models

import com.example.srodenas.example_with_catalogs.data.users.database.dao.AlertDao
import com.example.srodenas.example_with_catalogs.data.users.database.entities.AlertEntity
import com.example.srodenas.example_with_catalogs.domain.UserDataBaseSingleton
import com.example.srodenas.example_with_catalogs.domain.users.models.Profile

class RepositoryAlerts private constructor(private val alertDao: AlertDao) {

    companion object {
        val repo: RepositoryAlerts by lazy {
            RepositoryAlerts(UserDataBaseSingleton.alertsDao)
        }
    }

    // Inserta la alerta usando la entidad y devuelve el id generado
    suspend fun addAlertForRepository(newAlert: Alert): Long {
        val idUser = Profile.profile.user!!.id
        // Construimos la entidad con id = 0 para que Room la genere
        val alertEntity = AlertEntity(
            id = 0,
            userId = idUser,
            textShort = newAlert.textShort,
            message = newAlert.message,
            alertDate = newAlert.alertDate
        )
        return alertDao.insertAlert(alertEntity)
    }

    suspend fun deleteAlertForRepository(alert: Alert) {
        alertDao.deleteAlertById(alert.id)
    }

    suspend fun showAllAlerts(idUser: Int): List<Alert> {
        val listAlertEntity = alertDao.getAlertsForUser(idUser)
        return listAlertEntity.map { it.toEntity() }
    }

    suspend fun initAllAlertForTest(newAlert: Alert) {
        for (i in 0..10)
            addAlertForRepository(newAlert)
    }

    suspend fun showAlertById(id: Int): Alert = (alertDao.getAlertById(id))!!.toEntity()

    // Conversión de AlertEntity a Alert (modelo de dominio)
    fun AlertEntity.toEntity(): Alert {
        return Alert(
            id = this.id,
            userId = this.userId,
            textShort = this.textShort,
            message = this.message,
            alertDate = this.alertDate
        )
    }

    fun devAlertForPos(pos: Int) = ListAlerts.list.alerts.get(pos)
}
