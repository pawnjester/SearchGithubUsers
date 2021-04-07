package com.example.domain.model

data class GithubUser(
    val login: String,
    val avatarUrl: String,
    val githubUrl : String,
    val isFavorite: Boolean
)