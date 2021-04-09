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
    override suspend fun searchUsers(query: String, pageNumber: Int): List<GithubUserEntity> {

            val apiResponse = apiService.searchUsers( page = pageNumber, query = query).items
            return apiResponse.map {
                mapper.mapFromModel(it)
            }
//        return flow {
//            val apiResponse = apiService.searchUsers(query).items
//            emit(apiResponse.map {
//                mapper.mapFromModel(it)
//            })
//        }

    }

}