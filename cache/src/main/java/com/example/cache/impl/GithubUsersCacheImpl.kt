package com.example.cache.impl

import com.example.cache.mappers.GithubUsersCacheModelMapper
import com.example.cache.room.GithubUserDao
import com.example.data.contracts.cache.GithubCache
import com.example.data.models.GithubUserEntity
import javax.inject.Inject

class GithubUsersCacheImpl @Inject constructor(
    private val dao: GithubUserDao,
    private val mapper: GithubUsersCacheModelMapper
) : GithubCache {


    override suspend fun saveUser(user: GithubUserEntity) {
        dao.favoriteUser(mapper.mapToModel(user))
    }


}