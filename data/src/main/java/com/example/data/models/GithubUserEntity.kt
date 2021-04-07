package com.example.data.models

data class GithubUserEntity(
    val login: String,
    val avatarUrl: String,
    val githubUrl: String,
    val isFavorite: Boolean = false

)