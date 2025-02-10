package com.example.proyectoevaluable.ui.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectoevaluable.R
import com.example.proyectoevaluable.domain.cards.models.Card
import com.example.proyectoevaluable.ui.views.adapters.StaticTipsAdapter

class FishingTipsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_fishing_tips, container, false)
        recyclerView = view.findViewById(R.id.recyclerViewTips)
        setUpRecyclerView()
        return view
    }

    private fun setUpRecyclerView() {
        val tips = listOf(
            Card(
                username = "Consejo 1",
                password = "Utiliza cebo fresco para atraer más peces.",
                weight = "",
                photoUri = null
            ),
            Card(
                username = "Consejo 2",
                password = "Los mejores momentos para pescar son al amanecer y al atardecer.",
                weight = "",
                photoUri = null
            ),
            Card(
                username = "Consejo 3",
                password = "Usa una caña de pescar adecuada para el tipo de pez que deseas capturar.",
                weight = "",
                photoUri = null
            ),
            Card(
                username = "Consejo 4",
                password = "Lleva siempre un equipo de seguridad adecuado.",
                weight = "",
                photoUri = null
            ),
            Card(
                username = "Consejo 5",
                password = "Investiga sobre las condiciones del agua y el clima antes de salir.",
                weight = "",
                photoUri = null
            )
        )

        val adapter = StaticTipsAdapter(tips)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }
}
