package com.example.srodenas.simulacioncrud.Logic.interfaces

interface OperationsInterface {
    fun ClientAdd (id: Int, name: String, lastName: String, phone: String)
    fun ClientDel ( id: Int)
    fun ClientUpdate (id: Int, name: String, lastName: String, phone: String)
}
