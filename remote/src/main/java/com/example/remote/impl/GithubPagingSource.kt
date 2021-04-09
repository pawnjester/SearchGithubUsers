package com.example.remote.impl

import androidx.paging.PagingSource
import com.example.remote.ApiService
import com.example.remote.model.GithubUsersNetworkModel
import javax.inject.Inject

//class GithubPagingSource @Inject constructor(
//    private val apiService: ApiService
//) : PagingSource<Int, GithubUsersNetworkModel>() {
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GithubUsersNetworkModel> {
//        val pageNumber = params.key ?: 1
//        return try {
////            val response = apiService.searchUsers( pageNumber, "paw")
////            val data = response.items
////
////            LoadResult.Page(
////                data = data,
////                prevKey = pageNumber - 1,
////                nextKey = pageNumber + 1
////            )
//        } catch (e: Exception) {
//            LoadResult.Error(e)
//        }
//    }
//}