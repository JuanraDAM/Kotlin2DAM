package com.example.srodenas.example_with_catalogs.ui.views.fragments.users.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.srodenas.example_with_catalogs.databinding.ItemUserBinding
import com.example.srodenas.example_with_catalogs.domain.users.models.User

class UserAdapter(
    private var userList: List<User>,
    private val onDeleteClick: (User) -> Unit,
    private val onEditClick: (User) -> Unit
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    inner class UserViewHolder(val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.txtviewName.text = user.name
            binding.txtviewEmail.text = user.email
            binding.txtviewPhone.text = user.phone

            binding.btnDelete.setOnClickListener {
                onDeleteClick(user)
            }
            binding.btnEdit.setOnClickListener {
                onEditClick(user)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding =
            ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(userList[position])
    }

    override fun getItemCount(): Int = userList.size

    fun updateUsers(newUsers: List<User>) {
        userList = newUsers
        notifyDataSetChanged()
    }
}
