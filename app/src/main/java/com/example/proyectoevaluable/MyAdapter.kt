package com.example.proyectoevaluable

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.app.AlertDialog
import android.content.Context

class MyAdapter(
    private val context: Context,
    private val items: MutableList<Card>, // Cambiado a Card
    private val onDeleteConfirmed: (Int) -> Unit // Callback para confirmar la eliminación
) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val card = items[position] // Obtenemos el objeto Card
        holder.titleTextView.text = "Información del usuario"
        holder.descriptionTextView.text = "Usuario: ${card.username}\nContraseña: ${card.password}"
        holder.weightTextView.text = "Peso: --" // Personaliza este campo según sea necesario

        holder.deleteButton.setOnClickListener {
            // Mostrar un diálogo de confirmación
            AlertDialog.Builder(context)
                .setTitle("Confirmar eliminación")
                .setMessage("¿Estás seguro de que deseas eliminar esta tarjeta?")
                .setPositiveButton("Sí") { _, _ ->
                    onDeleteConfirmed(position) // Llamar al callback para confirmar la eliminación
                }
                .setNegativeButton("No", null) // Cerrar el diálogo sin acción
                .show()
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    // ViewHolder para referenciar las vistas de cada tarjeta
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.item_title)
        val descriptionTextView: TextView = itemView.findViewById(R.id.item_description)
        val weightTextView: TextView = itemView.findViewById(R.id.item_weight)
        val deleteButton: ImageButton = itemView.findViewById(R.id.delete_button)
    }
}
