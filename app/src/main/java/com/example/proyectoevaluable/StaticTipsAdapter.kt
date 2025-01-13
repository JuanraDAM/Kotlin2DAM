package com.example.proyectoevaluable

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StaticTipsAdapter(
    private val items: List<Card>
) : RecyclerView.Adapter<StaticTipsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_static_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val card = items[position]

        holder.titleTextView.text = card.username
        holder.descriptionTextView.text = card.password

        val weightText = card.weight?.let { "$it kg" } ?: "-- kg"
        holder.weightTextView.text = "Peso: $weightText"

        if (!card.photoUri.isNullOrEmpty()) {
            holder.imageView.setImageURI(Uri.parse(card.photoUri))
        } else {
            holder.imageView.setImageResource(R.drawable.logo)
        }
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.item_image)
        val titleTextView: TextView = itemView.findViewById(R.id.item_title)
        val descriptionTextView: TextView = itemView.findViewById(R.id.item_description)
        val weightTextView: TextView = itemView.findViewById(R.id.item_weight)
    }
}
