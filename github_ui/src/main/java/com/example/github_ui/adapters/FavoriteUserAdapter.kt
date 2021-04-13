package com.example.github_ui.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.github_ui.adapters.viewHolders.FavoriteUserViewHolder
import com.example.github_ui.models.GithubUsersModel
import com.example.github_ui.utils.FavoriteUserCacheCallback
import javax.inject.Inject

class FavoriteUserAdapter @Inject constructor(
) : RecyclerView.Adapter<FavoriteUserViewHolder>() {

    var openDetailsCallback: ((GithubUsersModel) -> Unit)? = null
    var favoriteUserCallback: FavoriteUserCacheCallback? = null
    private var favoriteUsers = mutableListOf<GithubUsersModel>()

    fun setFavoriteUsersList(items: List<GithubUsersModel>) {
        this.favoriteUsers.clear()
        this.favoriteUsers.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteUserViewHolder {
        return FavoriteUserViewHolder.create(
            parent,
            openDetailsCallback ?: {}, favoriteUserCallback!!
        )
    }

    override fun getItemCount(): Int = favoriteUsers.size

    override fun onBindViewHolder(holder: FavoriteUserViewHolder, position: Int) {
        holder.bind(favoriteUsers[position])
    }
}