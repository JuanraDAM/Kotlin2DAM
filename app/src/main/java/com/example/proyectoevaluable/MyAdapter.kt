package com.example.proyectoevaluable

import android.app.AlertDialog
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(
    private val context: Context,
    private val items: MutableList<Card>,
    private val onDeleteConfirmed: (Int) -> Unit,
    private val onEditClicked: (Int) -> Unit
) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val card = items[position]

        holder.titleTextView.text = card.username
        holder.descriptionTextView.text = card.password

        // Mostrar el peso en kg
        val weightInKg = card.weight?.let { "$it kg" } ?: "-- kg"
        holder.weightTextView.text = "Peso: $weightInKg"

        if (!card.photoUri.isNullOrEmpty()) {
            holder.imageView.setImageURI(Uri.parse(card.photoUri))
        } else {
            holder.imageView.setImageResource(R.drawable.logo)
        }

        holder.imageView.setOnClickListener {
            showImageDialog(card.photoUri)
        }

        holder.editButton.setOnClickListener {
            onEditClicked(position)
        }

        holder.deleteButton.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle("Confirmar eliminación")
                .setMessage("¿Estás seguro de que deseas eliminar esta tarjeta?")
                .setPositiveButton("Sí") { _, _ -> onDeleteConfirmed(position) }
                .setNegativeButton("No", null)
                .show()
        }
    }

    // Función para mostrar la imagen ampliada en un AlertDialog
    private fun showImageDialog(photoUri: String?) {
        if (photoUri.isNullOrEmpty()) {
            Toast.makeText(context, "No hay imagen para mostrar", Toast.LENGTH_SHORT).show()
            return
        }

        val dialog = AlertDialog.Builder(context).create()
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_image, null)
        val imageView = dialogView.findViewById<ImageView>(R.id.dialog_image_view)
        imageView.setImageURI(Uri.parse(photoUri))
        dialog.setView(dialogView)
        dialog.setCanceledOnTouchOutside(true)  // Permite cerrar al tocar fuera
        dialog.window?.setLayout(
            (context.resources.displayMetrics.widthPixels * 0.7).toInt(),
            (context.resources.displayMetrics.heightPixels * 0.7).toInt()
        )
        dialog.show()
    }

    override fun getItemCount(): Int = items.size

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.item_image)
        val titleTextView: TextView = itemView.findViewById(R.id.item_title)
        val descriptionTextView: TextView = itemView.findViewById(R.id.item_description)
        val weightTextView: TextView = itemView.findViewById(R.id.item_weight)
        val editButton: ImageButton = itemView.findViewById(R.id.edit_button)
        val deleteButton: ImageButton = itemView.findViewById(R.id.delete_button)
    }
}
