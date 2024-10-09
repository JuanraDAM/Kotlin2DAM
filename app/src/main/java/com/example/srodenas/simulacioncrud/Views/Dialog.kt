package com.example.srodenas.simulacioncrud.Views

import com.example.srodenas.simulacioncrud.Data.RepositoryClient
import com.example.srodenas.simulacioncrud.Logic.Client
import com.example.srodenas.simulacioncrud.Logic.interfaces.OperationsInterface

/*
class Dialog(var controller: Controller) {
    private var listener: OperationsInterface? = null  //Ya si eso lo creo.

    private var action : Int = 0

    fun setListener ( _listener : OperationsInterface){
        listener = _listener

    }

    //muestra el dialogo
    fun show(typeAction : Int){
        listener?.let{
            val posibleName = "CAMBIADO"
            val posibleId = controller.devIdRandom()
            when (typeAction){
                0 -> onAccept()

                1 ->
                    if (posibleId != -1)
                        onEdit(posibleId, "CAMBIADO")

                2 ->
                    if (posibleId != -1)
                        onDelete(posibleId)

            }

        }
    }

    private fun onDelete(id : Int) {
        listener!!.ClientDel(id)
    }

    private fun onEdit(id: Int, name : String) {
        listener!!.ClientUpdate(id, name)
    }

    private fun onAccept() {
        listener!!.ClientAdd(RepositoryClient.incrementPrimary(), "NUEVO CLIENTE")
    }
}*/

class Dialog {
    private var listener: OperationsInterface? = null

    fun setListener(_listener: OperationsInterface) {
        listener = _listener
    }

    fun show(typeAction: Int) {
        listener?.let {
            when (typeAction) {
                0 -> onAccept()
                1 -> onEdit()
                2 -> onDelete()
            }
        }
    }

    private fun onAccept() {
        val newClient = Client(RepositoryClient.incrementPrimary(), "NUEVO CLIENTE", "", "")
        listener!!.ClientAdd(newClient.id, newClient.name, newClient.lastName, newClient.phone)
    }

    private fun onEdit() {
        val posibleId = RepositoryClient.devIdRandom()
        if (posibleId != -1) {
            val client = RepositoryClient.arrayClient.find { it.id == posibleId }
            client?.let {
                listener!!.ClientUpdate(it.id, it.name, it.lastName, it.phone)
            }
        }
    }

    private fun onDelete() {
        val posibleId = RepositoryClient.devIdRandom()
        if (posibleId != -1) {
            listener!!.ClientDel(posibleId)
        }
    }
}