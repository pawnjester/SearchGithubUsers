package com.example.domain.repositories

import com.example.domain.model.GithubUser
import kotlinx.coroutines.flow.Flow

interface GithubUsersRepository {

    fun searchUsers(query: String) : Flow<List<GithubUser>>

    suspend fun favoriteUser(user: GithubUser)
}