package com.pmdm.virgen.dogapi.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.santi.pmdm.virgen.dogapicleanarchitecture.R
import com.santi.pmdm.virgen.dogapicleanarchitecture.domain.models.Repository
import com.santi.pmdm.virgen.dogapicleanarchitecture.domain.models.Dog

class DogAdapter(
    private val onDeleteClick: (Dog) -> Unit
) : RecyclerView.Adapter<ViewHDog>() {

    var dogRepository: List<Dog> = Repository.dogs

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHDog {
        val layoutInflater = LayoutInflater.from(parent.context)
        val layoutDogItem = R.layout.item_dog
        return ViewHDog(layoutInflater.inflate(layoutDogItem, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHDog, position: Int) {
        val dog = dogRepository[position]
        // Se pasa la lambda para la acción de eliminar
        holder.rendereize(dog, onDeleteClick)
    }

    override fun getItemCount() = dogRepository.size
}
