package com.example.remote.impl

import androidx.paging.PagingSource
import com.example.remote.model.GithubUsersNetworkModel
import javax.inject.Inject

class GithubPagingSource @Inject constructor() : PagingSource<Int, GithubUsersNetworkModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GithubUsersNetworkModel> {
        TODO("Not yet implemented")
    }
}