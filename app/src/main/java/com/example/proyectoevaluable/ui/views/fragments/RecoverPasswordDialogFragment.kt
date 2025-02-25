package com.example.proyectoevaluable.ui.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.example.proyectoevaluable.R
import com.example.proyectoevaluable.data.auth.AuthRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RecoverPasswordDialogFragment : DialogFragment() {

    @Inject
    lateinit var authRepository: AuthRepository

    private var userEmail: String = ""

    companion object {
        private const val ARG_USER_EMAIL = "arg_user_email"
        fun newInstance(userEmail: String): RecoverPasswordDialogFragment {
            val fragment = RecoverPasswordDialogFragment()
            val args = Bundle()
            args.putString(ARG_USER_EMAIL, userEmail)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userEmail = it.getString(ARG_USER_EMAIL) ?: ""
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_recover_password, container, false)
        val newPasswordEditText = view.findViewById<EditText>(R.id.newPasswordEditText)
        val updateButton = view.findViewById<Button>(R.id.updateButton)
        val cancelButton = view.findViewById<Button>(R.id.cancelButton)

        updateButton.setOnClickListener {
            val newPassword = newPasswordEditText.text.toString().trim()
            if (newPassword.isEmpty() || newPassword.length < 6) {
                Toast.makeText(requireContext(), "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
            } else {
                lifecycleScope.launch {
                    val result = authRepository.recoverPassword(userEmail, newPassword)
                    result.onSuccess {
                        Toast.makeText(requireContext(), "Contraseña actualizada", Toast.LENGTH_SHORT).show()
                    }.onFailure { e ->
                        Toast.makeText(requireContext(), "Error al actualizar la contraseña: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                    dismiss()
                }
            }
        }

        cancelButton.setOnClickListener {
            dismiss()
        }
        return view
    }
}
