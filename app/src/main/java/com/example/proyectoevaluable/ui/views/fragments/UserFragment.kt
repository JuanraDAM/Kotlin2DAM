package com.example.proyectoevaluable.ui.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.proyectoevaluable.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import com.example.proyectoevaluable.di.TokenManager

@AndroidEntryPoint
class UserFragment : Fragment() {

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user, container, false)

        val textViewUserInfo = view.findViewById<TextView>(R.id.textViewUserInfo)
        val buttonClose = view.findViewById<Button>(R.id.buttonClose)

        // Se obtiene el email del usuario desde TokenManager (almacenado tras login vía API)
        val userEmail = tokenManager.getUserEmail()
        textViewUserInfo.text = if (!userEmail.isNullOrEmpty()) {
            "Bienvenido: $userEmail"
        } else {
            "No hay usuario logueado"
        }

        buttonClose.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity?.findViewById<View>(R.id.viewOverlayDim)?.visibility = View.GONE
        activity?.findViewById<View>(R.id.fragmentContainer)?.visibility = View.GONE
    }
}
