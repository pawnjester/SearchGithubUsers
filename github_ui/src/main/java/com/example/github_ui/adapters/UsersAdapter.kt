package com.example.github_ui.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.github_ui.adapters.viewHolders.UserViewHolder
import com.example.github_ui.models.GithubUsersModel
import com.example.github_ui.utils.FavoriteUserCallback
import com.example.github_ui.utils.OpenUserDetailsCallback
import javax.inject.Inject

class UsersAdapter @Inject constructor() : RecyclerView.Adapter<UserViewHolder>() {

    private val listOfUsers = mutableListOf<GithubUsersModel>()
    var openUsersCallback: OpenUserDetailsCallback? = null
    var favoriteUsersCallback: FavoriteUserCallback? = null


    fun setUsers(items: List<GithubUsersModel>) {
        listOfUsers.clear()
        listOfUsers.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder.create(parent, openUsersCallback ?: {}, favoriteUsersCallback ?: {})
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(listOfUsers[position])
    }

    override fun getItemCount(): Int = listOfUsers.size
}