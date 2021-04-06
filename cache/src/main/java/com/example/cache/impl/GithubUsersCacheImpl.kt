package com.example.cache.impl

import com.example.data.contracts.cache.GithubCache
import javax.inject.Inject

class GithubUsersCacheImpl @Inject constructor() : GithubCache {

    override suspend fun saveUser() {
        TODO("Not yet implemented")
    }
}