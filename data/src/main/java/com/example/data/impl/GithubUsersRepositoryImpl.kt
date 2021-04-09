package com.example.data.impl

import com.example.data.contracts.cache.GithubCache
import com.example.data.contracts.remote.GithubRemote
import com.example.data.mappers.GithubUserEntityMapper
import com.example.domain.model.GithubUser
import com.example.domain.repositories.GithubUsersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GithubUsersRepositoryImpl @Inject constructor(
    private val mapper: GithubUserEntityMapper,
    private val usersRemote: GithubRemote,
    private val usersCache: GithubCache
) : GithubUsersRepository {
    override fun searchUsers(query: String, pageNumber: Int): Flow<List<GithubUser>> {

        return flow {
            emit(usersRemote.searchUsers(query, pageNumber).map {
                mapper.mapFromEntity(it)
            }
                .onEach { user: GithubUser ->
                    val checkUser: Boolean = usersCache.checkIfUserExist(user.id)
                    if (checkUser) {
                        user.apply { isFavorite = true }
                    } else {
                        user.apply { isFavorite = false }
                    }
                })
        }
    }

    override suspend fun favoriteUser(user: GithubUser) {
        usersCache.saveUser(mapper.mapToEntity(user))
    }

    override fun getFavoriteUsers(): Flow<List<GithubUser>> {
        return flow {
            emitAll(usersCache.getUsers().map {
                mapper.mapFromEntityList(it)
            })
        }
    }
}