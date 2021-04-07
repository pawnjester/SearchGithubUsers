package com.example.github_ui.adapters.viewHolders

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.github_ui.R
import com.example.github_ui.databinding.ItemUsersBinding
import com.example.github_ui.models.GithubUsersModel
import com.example.github_ui.utils.FavoriteUserCallback
import com.example.github_ui.utils.OpenUserDetailsCallback
import com.example.github_ui.utils.loadImage

class FavoriteUserViewHolder(
    private val binding: ItemUsersBinding,
    private val openUsersDetailsCallback: OpenUserDetailsCallback,
    private val favoriteUserCallback: FavoriteUserCallback,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: GithubUsersModel) {
        val iconDrawable = setFavoriteIconColor(item)

        item.run {
            binding.imageUser.loadImage(avatarUrl)
            binding.userName.text = login

            binding.userLayout.setOnClickListener {
                openUsersDetailsCallback.invoke(item)
            }
            binding.favoriteUser.setImageDrawable(iconDrawable)
            binding.favoriteUser.setOnClickListener {
                favoriteUserCallback.invoke(item)
            }
        }

    }


    private fun setFavoriteIconColor(item: GithubUsersModel): Drawable? {
        return when {
            item.isFavorite -> {
                ResourcesCompat.getDrawable(itemView.resources, R.drawable.ic_red_favorite, null)
            }
            else -> ResourcesCompat.getDrawable(itemView.resources, R.drawable.ic_baseline_favorite_border_24, null)
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            openUsersDetailsCallback: OpenUserDetailsCallback,
            favoriteUserCallback: FavoriteUserCallback
        ): FavoriteUserViewHolder {
            val binding = ItemUsersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return FavoriteUserViewHolder(binding, openUsersDetailsCallback, favoriteUserCallback)
        }
    }
}