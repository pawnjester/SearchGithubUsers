package com.example.domain.repositories

import com.example.domain.model.GithubUser
import kotlinx.coroutines.flow.Flow

interface GithubUsersRepository {

    fun searchUsers(query: String, pageNumber: Int = 0) : Flow<List<GithubUser>>

    suspend fun favoriteUser(user: GithubUser)

    fun getFavoriteUsers() : Flow<List<GithubUser>>
}