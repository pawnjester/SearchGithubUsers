package com.example.data.contracts.cache

import com.example.data.models.GithubUserEntity
import kotlinx.coroutines.flow.Flow

interface GithubCache {

    suspend fun saveUser(user: GithubUserEntity)

    fun getUsers() : Flow<List<GithubUserEntity>>
}