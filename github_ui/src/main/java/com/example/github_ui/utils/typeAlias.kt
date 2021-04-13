package com.example.github_ui.utils

import com.example.github_ui.models.GithubUsersModel

typealias OpenUserDetailsCallback = (GithubUsersModel) -> Unit
typealias FavoriteUserCallback = (GithubUsersModel, isFavorite: Boolean) -> Unit
typealias FavoriteUserCacheCallback = (GithubUsersModel) -> Unit
