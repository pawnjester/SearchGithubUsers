package com.example.github_ui.adapters.viewHolders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.github_ui.databinding.ItemUsersBinding
import com.example.github_ui.models.GithubUsersModel
import com.example.github_ui.utils.OpenUserDetailsCallback
import com.example.github_ui.utils.loadImage

class UserViewHolder(
    private val binding: ItemUsersBinding,
    private val openUsersDetailsCallback: OpenUserDetailsCallback
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: GithubUsersModel) {
        item.run {
            binding.imageUser.loadImage(avatarUrl)
            binding.userName.text = login

            binding.userLayout.setOnClickListener {
                openUsersDetailsCallback.invoke(item)
            }
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            openUsersDetailsCallback: OpenUserDetailsCallback
        ): UserViewHolder {
            val binding = ItemUsersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return UserViewHolder(binding, openUsersDetailsCallback)
        }
    }
}