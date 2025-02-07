package com.example.srodenas.example_with_catalogs.ui.views.fragments.alerts.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.srodenas.example_with_catalogs.databinding.ItemAlertBinding
import com.example.srodenas.example_with_catalogs.domain.alerts.models.Alert

class AdapterAlerts(
    private var alerts: MutableList<Alert>,
    private val deleteAction: (Int) -> Unit,
    private val detailsAction: (Int) -> Unit
) : RecyclerView.Adapter<AdapterAlerts.ViewHolder>() {

    inner class ViewHolder(val binding: ItemAlertBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(alert: Alert, position: Int) {
            binding.txtNameAlert.text = alert.textShort

            // Al pulsar la CardView completa (opcional) mostramos detalles.
            binding.root.setOnClickListener { detailsAction(position) }

            // Botón para ver detalles
            binding.btnDetailsAlert.setOnClickListener { detailsAction(position) }

            // Botón para eliminar la alerta
            binding.btnDeleteAlert.setOnClickListener { deleteAction(position) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAlertBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(alerts[position], position)
    }

    override fun getItemCount(): Int = alerts.size

    fun updateData(newAlerts: List<Alert>) {
        alerts.clear()
        alerts.addAll(newAlerts)
        notifyDataSetChanged()
    }
}
