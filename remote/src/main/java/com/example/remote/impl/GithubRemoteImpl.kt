package com.example.remote.impl

import com.example.data.contracts.remote.GithubRemote
import com.example.data.models.GithubUserEntity
import com.example.remote.ApiService
import com.example.remote.mappers.RemoteNetworkModelMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GithubRemoteImpl @Inject constructor(
    private val mapper: RemoteNetworkModelMapper,
    private val apiService: ApiService
) : GithubRemote {
    override fun searchUsers(query: String): Flow<List<GithubUserEntity>> {
        return flow {
            val apiResponse = apiService.searchUsers(query).items
            emit(apiResponse.map {
                mapper.mapFromModel(it)
            })
        }
    }

}