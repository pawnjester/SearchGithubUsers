package com.example.searchgithubusers.fakes

import com.example.data.models.GithubUserEntity
import com.example.domain.model.GithubUser

object DummyData {

    fun makeGithubUser(): GithubUser {
        return GithubUser(1, "name", "picture", "url", false)
    }

    fun makeGithubUserList(count: Int) : List<GithubUser> {
        val list = mutableListOf<GithubUser>()
        repeat(count) {
            list.add(makeGithubUser())
        }
        return list
    }
}