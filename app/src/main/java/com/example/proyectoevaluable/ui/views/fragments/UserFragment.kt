package com.example.proyectoevaluable.ui.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.proyectoevaluable.R
import com.google.firebase.auth.FirebaseAuth

class UserFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user, container, false)

        val textViewUserInfo = view.findViewById<TextView>(R.id.textViewUserInfo)
        val buttonClose = view.findViewById<Button>(R.id.buttonClose)

        val currentUser = FirebaseAuth.getInstance().currentUser
        textViewUserInfo.text = if (currentUser != null) {
            "Bienvenido: ${currentUser.email}"
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
