package com.example.data.contracts.remote

import com.example.data.models.GithubUserResponseEntity

interface GithubRemote {
    suspend fun searchUsers(query: String, pageNumber: Int): GithubUserResponseEntity
}