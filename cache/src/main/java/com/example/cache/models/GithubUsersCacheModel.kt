package com.example.cache.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.cache.models.GithubUsersCacheModel.Companion.TABLE_NAME


@Entity(tableName = TABLE_NAME)
data class GithubUsersCacheModel(
    @PrimaryKey
    val login: String,
    val avatarUrl: String,
    val githubUrl: String,
    val isFavorite: Boolean = false
) {
    companion object {
        const val TABLE_NAME = "users"
    }
}