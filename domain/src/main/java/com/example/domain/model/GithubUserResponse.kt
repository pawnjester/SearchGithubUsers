package com.example.domain.model

data class GithubUserResponse(
    val total_count: Int,
    val items: List<GithubUser>
)

