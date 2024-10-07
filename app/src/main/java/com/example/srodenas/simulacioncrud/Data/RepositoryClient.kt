package com.example.srodenas.simulacioncrud.Data

import com.example.srodenas.simulacioncrud.Logic.Client


class RepositoryClient {

    companion object {
        var primary = 100

        val arrayClient : List<Client> = listOf (
            Client (RepositoryClient.incrementPrimary(), "Santi"),
            Client (RepositoryClient.incrementPrimary(), "Sonia"),
            Client (RepositoryClient.incrementPrimary(), "Guille"),
            Client (RepositoryClient.incrementPrimary(), "Diego")
        )

        fun incrementPrimary() = primary ++
    }

}
