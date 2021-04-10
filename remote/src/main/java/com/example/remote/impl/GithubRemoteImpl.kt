package com.example.remote.impl

import com.example.data.contracts.remote.GithubRemote
import com.example.data.models.GithubUserResponseEntity
import com.example.remote.ApiService
import com.example.remote.mappers.GithubResponseNetworkMapper
import javax.inject.Inject

class GithubRemoteImpl @Inject constructor(
    private val mapper: GithubResponseNetworkMapper,
    private val apiService: ApiService
) : GithubRemote {
    override suspend fun searchUsers(query: String, pageNumber: Int): GithubUserResponseEntity {

        val apiResponse = apiService.searchUsers(page = pageNumber, query = query)
        return mapper.mapFromModel(apiResponse)

    }

}