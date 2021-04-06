package com.example.github_ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.github_ui.models.GithubUsersModel
import javax.inject.Inject

class DetailViewModel @Inject constructor(

) : ViewModel() {

    private var userItem: GithubUsersModel? = null

    private var _user = MutableLiveData<GithubUsersModel>()
    var user: LiveData<GithubUsersModel> = _user


    fun setUserDetail(user: GithubUsersModel?) {
        user?.let {
            userItem = it
            _user.value = it
        }
    }

}