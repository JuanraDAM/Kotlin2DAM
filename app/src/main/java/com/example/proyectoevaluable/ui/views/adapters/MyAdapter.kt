package com.example.proyectoevaluable.ui.views.adapters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectoevaluable.R
import com.example.proyectoevaluable.domain.cards.models.Card

class MyAdapter(
    private val context: Context,
    private val items: MutableList<Card>,
    private val onDeleteConfirmed: (Int) -> Unit,
    private val onEditClicked: (Int) -> Unit,
    private val onMapsClicked: (Int) -> Unit
) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_card, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val card = items[position]
        holder.titleTextView.text = card.title
        holder.descriptionTextView.text = card.description

        val weightInKg = "${card.weight} kg"
        holder.weightTextView.text = "Peso: $weightInKg"

        if (!card.image.isNullOrEmpty()) {
            val bitmap = decodeBase64ToBitmap(card.image)
            if (bitmap != null) {
                holder.imageView.setImageBitmap(bitmap)
            } else {
                holder.imageView.setImageResource(R.drawable.logo)
            }
        } else {
            holder.imageView.setImageResource(R.drawable.logo)
        }

        holder.imageView.setOnClickListener {
            showImageDialog(card.image)
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

        holder.mapsButton.setOnClickListener {
            onMapsClicked(position)
        }
    }

    fun decodeBase64ToBitmap(base64Str: String): Bitmap? {
        return try {
            val decodedBytes = Base64.decode(base64Str, Base64.NO_WRAP)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun showImageDialog(image: String?) {
        if (image.isNullOrEmpty()) {
            Toast.makeText(context, "No hay imagen para mostrar", Toast.LENGTH_SHORT).show()
            return
        }
        val bitmap = decodeBase64ToBitmap(image)
        if (bitmap == null) {
            Toast.makeText(context, "Error al mostrar la imagen", Toast.LENGTH_SHORT).show()
            return
        }
        val dialog = AlertDialog.Builder(context).create()
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_image, null)
        val imageView = dialogView.findViewById<ImageView>(R.id.dialog_image_view)
        imageView.setImageBitmap(bitmap)
        dialog.setView(dialogView)
        dialog.setCanceledOnTouchOutside(true)
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
        val mapsButton: ImageButton = itemView.findViewById(R.id.maps_button)
    }
}
