package com.example.srodenas.example_with_catalogs.ui.views.fragments.users.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.srodenas.example_with_catalogs.databinding.ItemUserBinding
import com.example.srodenas.example_with_catalogs.domain.users.models.User

class ViewHUser (view: View,
                 val onDetails: (Int) -> Unit

) : RecyclerView.ViewHolder(view){

    lateinit var binding: ItemUserBinding
    init {
        binding = ItemUserBinding.bind(view)
    }


    
}