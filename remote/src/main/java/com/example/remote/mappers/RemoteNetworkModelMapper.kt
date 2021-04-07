package com.example.remote.mappers

import com.example.data.models.GithubUserEntity
import com.example.remote.mappers.base.RemoteModelMapper
import com.example.remote.model.GithubUsersNetworkModel
import javax.inject.Inject

class RemoteNetworkModelMapper @Inject constructor() : RemoteModelMapper<GithubUsersNetworkModel, GithubUserEntity> {
    override fun mapFromModel(model: GithubUsersNetworkModel): GithubUserEntity {
        return model.run {
            GithubUserEntity(
                login,
                avatar_url,
                html_url
            )
        }
    }
}