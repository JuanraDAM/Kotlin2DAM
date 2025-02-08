package com.pmdm.virgen.dogapi.ui.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.santi.pmdm.virgen.dogapicleanarchitecture.databinding.ItemDogBinding
import com.santi.pmdm.virgen.dogapicleanarchitecture.domain.models.Dog

class ViewHDog(view: View) : RecyclerView.ViewHolder(view) {

    private val binding: ItemDogBinding = ItemDogBinding.bind(view)

    /**
     * Método que asigna los datos del perro y configura el botón de eliminación.
     * @param dog: Objeto Dog a renderizar.
     * @param onDelete: Lambda que se invoca cuando se pulsa el botón de eliminar.
     */
    fun rendereize(dog: Dog, onDelete: (Dog) -> Unit) {
        // Cargar la imagen
        Glide.with(itemView.context)
            .load(dog.image)
            .centerCrop()
            .into(binding.ivImagen)

        // Configurar el botón de eliminar
        binding.btnDelete.setOnClickListener {
            onDelete(dog)
        }
    }
}
