package com.example.srodenas.example_with_catalogs.domain.alerts.usecase

import com.example.srodenas.example_with_catalogs.domain.alerts.models.ListAlerts
import com.example.srodenas.example_with_catalogs.domain.alerts.models.RepositoryAlerts
import com.example.srodenas.example_with_catalogs.domain.users.models.Profile

class UseCaseShowAlerts (val repositoryAlerts: RepositoryAlerts) {
    suspend fun showAlerts() = repositoryAlerts.showAllAlerts(Profile.profile.user!!.id).toMutableList()


}