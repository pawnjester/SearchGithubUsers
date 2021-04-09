package com.example.remote

import com.example.remote.model.GithubUsersResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("users")
    suspend fun searchUsers(
        @Query("per_page") per : Int = 10,
        @Query("page") page: Int = 1,
        @Query("q") query : String
    ) : GithubUsersResponse
}