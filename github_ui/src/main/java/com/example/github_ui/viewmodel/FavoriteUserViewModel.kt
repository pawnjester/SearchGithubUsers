package com.example.github_ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecases.FavoriteUserUseCase
import com.example.domain.usecases.GetFavoriteUsersUseCase
import com.example.github_ui.mappers.GithubUsersModelMapper
import com.example.github_ui.models.GithubUsersModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteUserViewModel @Inject constructor(
    private val getUsers: GetFavoriteUsersUseCase,
    private val favoriteUserUseCase: FavoriteUserUseCase,
    private val mapper: GithubUsersModelMapper,
) : ViewModel() {


    private val _favoriteUsers = MutableLiveData<LatestUiState<List<GithubUsersModel>>>()
    val favoriteUsers: LiveData<LatestUiState<List<GithubUsersModel>>> = _favoriteUsers

    fun getFavoriteUsers() {
        viewModelScope.launch {
            getUsers().map {
                mapper.mapToModelList(it)
            }.collect {
                _favoriteUsers.value = LatestUiState.Success(it)
            }

        }
    }

    fun favoriteUser(user: GithubUsersModel) {
        viewModelScope.launch {
            val favoriteRestaurant = user.apply { isFavorite = !user.isFavorite }
            favoriteUserUseCase(mapper.mapToDomain(favoriteRestaurant))
        }
    }
}