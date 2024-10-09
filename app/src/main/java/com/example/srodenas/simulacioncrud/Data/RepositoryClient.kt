package com.example.srodenas.simulacioncrud.Data

import com.example.srodenas.simulacioncrud.Logic.Client
import kotlin.random.Random


class RepositoryClient {
    companion object {
        var primary = 100

        val arrayClient: MutableList<Client> = mutableListOf()

        init {
            arrayClient.add(Client(incrementPrimary(), "Santi", "", ""))
            arrayClient.add(Client(incrementPrimary(), "Sonia", "", ""))
            arrayClient.add(Client(incrementPrimary(), "Guille", "", ""))
            arrayClient.add(Client(incrementPrimary(), "Diego", "", ""))
        }

        fun incrementPrimary(): Int {
            return primary++
        }

        fun devIdRandom(): Int {
            return if (arrayClient.size == 0) {
                -1
            } else {
                val p = Random.nextInt(0, arrayClient.size)
                arrayClient[p].id
            }
        }
    }
}
