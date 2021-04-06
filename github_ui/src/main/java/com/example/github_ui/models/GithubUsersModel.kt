package com.example.github_ui.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GithubUsersModel(
    val login: String,
    val avatarUrl: String
) : Parcelable