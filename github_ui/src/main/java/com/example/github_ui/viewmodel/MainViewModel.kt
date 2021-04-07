package com.example.github_ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecases.FavoriteUserUseCase
import com.example.domain.usecases.SearchUsersUseCase
import com.example.github_ui.mappers.GithubUsersModelMapper
import com.example.github_ui.models.GithubUsersModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val searchUsers: SearchUsersUseCase,
    private val favoriteUserUseCase: FavoriteUserUseCase,
    private val mapper: GithubUsersModelMapper
) : ViewModel() {

    private val _users = MutableLiveData<LatestUiState<List<GithubUsersModel>>>()
    val users: LiveData<LatestUiState<List<GithubUsersModel>>> = _users

    fun searchGithubUsers(query: String) {
        _users.value = LatestUiState.Loading
        viewModelScope.launch {
            searchUsers(query).map {
                mapper.mapToModelList(it)
            }.debounce(3000)
                .collect {
                    _users.value = LatestUiState.Success(it)
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

sealed class LatestUiState<out T : Any> {
    data class Success<out T : Any>(val users: T) : LatestUiState<T>()
    object Loading : LatestUiState<Nothing>()
    data class Error(val exception: String) : LatestUiState<Nothing>()
}