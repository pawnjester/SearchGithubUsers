package com.example.searchgithubusers.fakes

import com.example.domain.model.GithubUser
import com.example.domain.model.GithubUserResponse
import com.example.domain.repositories.GithubUsersRepository
import com.example.searchgithubusers.fakes.DummyData.makeGithubUser
import com.example.searchgithubusers.fakes.DummyData.makeGithubUserList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeGithubRepository : GithubUsersRepository {
    private val cache = LinkedHashMap<String, GithubUser>()

    override fun searchUsers(query: String, pageNumber: Int): Flow<GithubUserResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun favoriteUser(user: GithubUser) {
        cache[user.login] = user
    }

    override suspend fun deleteFavoriteUser(user: GithubUser) {
        TODO("Not yet implemented")
    }

    override fun getFavoriteUsers(): Flow<List<GithubUser>> {
        return flowOf(makeGithubUserList(1))
    }

    override fun checkFavoriteUser(id: Int): Flow<Boolean> {
        TODO("Not yet implemented")
    }
}