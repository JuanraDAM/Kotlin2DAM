package com.example.srodenas.example_with_catalogs.ui.views.fragments.users.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.srodenas.example_with_catalogs.R
import com.example.srodenas.example_with_catalogs.databinding.UserRegisterDialogBinding
import com.example.srodenas.example_with_catalogs.domain.users.models.User

class DialogRegisterUser(
    private val onNewUserDialog: (User) -> Unit
) : DialogFragment() {

    private var _binding: UserRegisterDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = UserRegisterDialogBinding.inflate(LayoutInflater.from(context))
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setView(binding.root)
                .setPositiveButton("Registrar Usuario") { dialog, _ ->
                    val newUser = recoverData()
                    if (newUser.name.isEmpty() || newUser.email.isEmpty() || newUser.password.isEmpty()) {
                        Toast.makeText(activity, "Algún campo está vacío", Toast.LENGTH_LONG).show()
                        dialog.cancel()
                    } else {
                        onNewUserDialog(newUser)
                    }
                }
                .setNegativeButton("Cancelar") { dialog, _ ->
                    dialog.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun recoverData(): User {
        return User(
            id = 0,
            name = binding.txtViewName.text.toString(),
            email = binding.txtViewEmail.text.toString(),
            password = binding.txtViewPassword.text.toString(), // Usamos 'password' en lugar de 'passw'
            phone = binding.txtViewPhone.text.toString(),
            imag = binding.txtViewUrlImage.text.toString()      // Usamos 'imag' en lugar de 'imagen'
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
