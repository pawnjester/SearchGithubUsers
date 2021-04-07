package com.example.data.contracts.cache

import com.example.data.models.GithubUserEntity

interface GithubCache {

    suspend fun saveUser(user: GithubUserEntity)
}