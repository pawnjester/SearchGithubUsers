package com.example.remote

import com.example.remote.model.GithubUsersResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("users")
    suspend fun searchUsers(
        @Query("q") q : String
    ) : GithubUsersResponse
}