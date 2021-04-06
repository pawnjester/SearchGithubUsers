package com.example.github_ui.mappers

import com.example.domain.model.GithubUser
import com.example.github_ui.models.GithubUsersModel
import javax.inject.Inject

class GithubUsersModelMapper @Inject constructor(
) : ModelMapper<GithubUsersModel, GithubUser> {

    override fun mapToModel(domain: GithubUser): GithubUsersModel {
        return domain.run {
            GithubUsersModel(
                domain.login,
                domain.avatarUrl
            )
        }
    }

    override fun mapToDomain(model: GithubUsersModel): GithubUser {
        return model.run {
            GithubUser(
                model.login,
                model.avatarUrl
            )
        }
    }
}