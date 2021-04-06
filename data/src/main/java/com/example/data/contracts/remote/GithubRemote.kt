package com.example.data.contracts.remote

import com.example.data.models.GithubUserEntity
import kotlinx.coroutines.flow.Flow

interface GithubRemote {
    fun searchUsers(query: String) : Flow<List<GithubUserEntity>>
}