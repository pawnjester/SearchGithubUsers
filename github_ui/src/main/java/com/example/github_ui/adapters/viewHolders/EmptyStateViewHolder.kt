package com.example.github_ui.adapters.viewHolders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.github_ui.R
import com.example.github_ui.databinding.EmptyStateViewholderBinding

class EmptyStateViewHolder(private val binding: EmptyStateViewholderBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind() {
        binding.run {
            animationView.setAnimation(R.raw.empty_state)
            animationView.playAnimation()
        }
    }

    companion object {
        fun create(parent: ViewGroup): EmptyStateViewHolder {
            val binding = EmptyStateViewholderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return EmptyStateViewHolder(binding)
        }
    }

}