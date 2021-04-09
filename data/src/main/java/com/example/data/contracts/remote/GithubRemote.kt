package com.example.data.contracts.remote

import com.example.data.models.GithubUserEntity

interface GithubRemote {
    suspend fun searchUsers(query: String, pageNumber: Int): List<GithubUserEntity>
//    fun searchUsers(query: String) : List<GithubUserEntity>
}