package com.example.srodenas.example_with_catalogs.ui.views.fragments.users

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.srodenas.example_with_catalogs.databinding.FragmentUsersBinding
import com.example.srodenas.example_with_catalogs.domain.users.models.User
import com.example.srodenas.example_with_catalogs.ui.viewmodel.users.UserViewModel
import com.example.srodenas.example_with_catalogs.ui.views.fragments.users.adapter.UserAdapter

class UsersFragment : Fragment() {

    private var _binding: FragmentUsersBinding? = null
    private val binding get() = _binding!!

    private val userViewModel: UserViewModel by viewModels({ requireActivity() })

    private lateinit var userAdapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUsersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userAdapter = UserAdapter(
            emptyList(),
            onDeleteClick = { user ->
                userViewModel.deleteUser(user)
            },
            onEditClick = { user ->
                showEditNameDialog(user)
            }
        )

        binding.recyclerViewUsers.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = userAdapter
        }

        userViewModel.usersLiveData.observe(viewLifecycleOwner) { users ->
            userAdapter.updateUsers(users)
        }

        userViewModel.showUsers()
    }

    /**
     * Muestra un diálogo para editar el nombre del usuario.
     */
    private fun showEditNameDialog(user: User) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Editar Nombre")
        val input = EditText(requireContext())
        input.inputType = InputType.TYPE_CLASS_TEXT
        input.setText(user.name)
        builder.setView(input)
        builder.setPositiveButton("Guardar") { dialog, _ ->
            val newName = input.text.toString().trim()
            if (newName.isNotEmpty()) {
                userViewModel.updateUserName(user.id, newName)
            }
        }
        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.cancel()
        }
        builder.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
