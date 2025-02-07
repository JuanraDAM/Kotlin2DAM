package com.example.srodenas.example_with_catalogs.ui.views.fragments.alerts.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.srodenas.example_with_catalogs.databinding.ItemAlertBinding
import com.example.srodenas.example_with_catalogs.domain.alerts.models.Alert
import com.example.srodenas.example_with_catalogs.domain.users.models.User

class ViewHAlert (view: View,
                  val onDelete: (Int) -> Unit,
                  val onDetails: (Int) -> Unit

) : RecyclerView.ViewHolder(view){

    lateinit var binding: ItemAlertBinding
    init {
        binding = ItemAlertBinding.bind(view)
    }


    fun renderize(alert: Alert, position:Int){
        binding.btnDeleteAlert.setOnClickListener{
            onDelete(position)
        }

        binding.btnDetailsAlert.setOnClickListener{
            onDetails(position)
        }

    }
}