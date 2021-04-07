package com.example.github_ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecases.FavoriteUserUseCase
import com.example.github_ui.mappers.GithubUsersModelMapper
import com.example.github_ui.models.GithubUsersModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val mapper: GithubUsersModelMapper,
    private val favoriteUserUseCase: FavoriteUserUseCase
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

    fun favoriteUser(user: GithubUsersModel) {
        viewModelScope.launch {
            val favoriteRestaurant = user.apply { isFavorite = !user.isFavorite }
            favoriteUserUseCase(mapper.mapToDomain(favoriteRestaurant))
            _user.value = favoriteRestaurant
        }
    }

}