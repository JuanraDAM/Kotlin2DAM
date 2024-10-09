package com.example.srodenas.simulacioncrud.Views

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.srodenas.simulacioncrud.Logic.Client
import com.example.srodenas.simulacioncrud.Logic.Controller
import com.example.srodenas.simulacioncrud.Logic.interfaces.OperationsInterface
import com.example.srodenas.simulacioncrud.R

class MainActivity : AppCompatActivity(), OperationsInterface {
    private lateinit var myButtonAdd: ImageView
    private lateinit var myButtonUpdate: ImageView
    private lateinit var myButtonDel: ImageView
    private lateinit var myDialog : Dialog
    private val controller= Controller()

    companion object {
        const val TAG = "---SALIDA---"
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log. d(TAG, "Esto es un ejemplo de log")
        start()
    }


    private fun start() {
        myButtonAdd = findViewById(R.id.myButtonAdd)
        myButtonUpdate = findViewById(R.id.myButtomEdit)
        myButtonDel = findViewById(R.id.myButtomDel)
        myDialog = Dialog()

        myDialog.setListener(this)
        myButtonAdd.setOnClickListener{
            myDialog.show(0)
        }

        myButtonUpdate.setOnClickListener{
            myDialog.show(1)
        }

        myButtonDel.setOnClickListener( {
            myDialog.show(2)
        })


    }



    override fun ClientAdd(id: Int, name: String, lastName: String, phone: String) {
        val newClient = Client(id, name, lastName, phone)
        controller.ClientAddController(newClient)
        var msg = "El cliente con id = $id, ha sido insertado correctamente"
        Log.d(TAG, msg)
        showConsoleData(msg)
    }

    override fun ClientDel(id: Int) {
        var msg = ""
        val delete = controller.ClientDelController(id)

        if (delete)
             msg =  "El cliente con id = $id, ha sido eliminado correctamente"
        else
            msg = "El cliente con id = $id, no ha sido encontrado para eliminar"

        Log. d(TAG, msg)
        showConsoleData(msg)

    }


    override fun ClientUpdate(id: Int, name: String, lastName: String, phone: String) {
        var msg = ""
        val update = controller.ClientUpdateController(id, name, lastName, phone)
        if (update) {
            msg = "El cliente con id = $id, ha sido actualizado correctamente"
        } else {
            msg = "El cliente con id = $id, no ha sido encontrado para actualizar"
        }
        Log.d(TAG, msg)
        showConsoleData(msg)
    }

    fun showConsoleData(msg:String){
        val msg = controller.showData()
        Thread.sleep(2000)
        Log. d(TAG, msg)
    }
}