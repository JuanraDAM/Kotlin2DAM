package com.example.srodenas.example_with_catalogs.ui.views.fragments.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.srodenas.example_with_catalogs.databinding.FragmentProfileBinding
import com.example.srodenas.example_with_catalogs.domain.users.models.Profile
import com.example.srodenas.example_with_catalogs.ui.views.activities.LoginActivity
import com.example.srodenas.example_with_catalogs.ui.viewmodel.users.UserViewModel
import androidx.fragment.app.viewModels

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    // Se comparte el ViewModel con la Activity (si es necesario)
    private val userViewModel: UserViewModel by viewModels({ requireActivity() })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Muestra los datos del usuario, si está inicializado
        Profile.profile.user?.let { user ->
            binding.txtUserName.text = user.name
            binding.txtUserEmail.text = user.email
        }
        // Configura el botón de logout
        binding.btnLogout.setOnClickListener {
            // Realiza el logout
            userViewModel.logout(this.requireContext())

            // Lanza la LoginActivity con las banderas para limpiar la pila
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
