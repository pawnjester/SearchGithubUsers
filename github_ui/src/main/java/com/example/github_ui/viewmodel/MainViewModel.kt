package com.example.github_ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.GithubUser
import com.example.domain.usecases.FavoriteUserUseCase
import com.example.domain.usecases.GetFavoriteUsersUseCase
import com.example.domain.usecases.LoadMoreUsersUseCase
import com.example.domain.usecases.SearchUsersUseCase
import com.example.github_ui.mappers.GithubUsersModelMapper
import com.example.github_ui.models.GithubUsersModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val searchUsers: SearchUsersUseCase,
    private val loadMore: LoadMoreUsersUseCase,
    private val favoriteUserUseCase: FavoriteUserUseCase,
    private val getFavoriteUseCase: GetFavoriteUsersUseCase,
    private val mapper: GithubUsersModelMapper
) : ViewModel() {

    private val _users = MutableLiveData<LatestUiState<List<GithubUsersModel>>>()
    val users: LiveData<LatestUiState<List<GithubUsersModel>>> = _users

    private val usersList = mutableListOf<GithubUsersModel>()

    private var params: SearchUsersUseCase.Params? = null

    private var currentPage: Int = 1

    private var queryItem: String = ""

//    private val _queryItem = MutableLiveData<String>()
//    val queryItem: LiveData<String> = _queryItem

    fun setQueryInfo(query: String) {
        queryItem = query
    }

    fun searchGithubUsers() {
        _users.value = LatestUiState.Loading
        params = SearchUsersUseCase.Params(
            query = queryItem ?: "",
            pageNumber = currentPage
        )
        viewModelScope.launch {
            searchUsers(params).combine(getFavoriteUseCase())
            { searchItems: List<GithubUser>, favoritesItem: List<GithubUser> ->
                searchItems.onEach { user ->
                    val favoriteItem = favoritesItem.find { it.id == user.id }
                    val exist = favoriteItem != null
                    user.isFavorite = exist
                }
            }
                .map {
                    mapper.mapToModelList(it)
                }
                .collect {
                    usersList.clear()
                    usersList.addAll(it)
                    _users.value = LatestUiState.Success(it)
                }
        }
    }

    fun fetchMoreUsers() {
        params = SearchUsersUseCase.Params(
            query = queryItem,
            pageNumber = currentPage + 1
        )
        viewModelScope.launch {
            loadMore(params).combine(getFavoriteUseCase())
            { searchItems: List<GithubUser>, favoritesItem: List<GithubUser> ->
                searchItems.onEach { user ->
                    val favoriteItem = favoritesItem.find { it.id == user.id }
                    val exist = favoriteItem != null
                    user.isFavorite = exist
                }
            }
                .map {
                    mapper.mapToModelList(it)
                }
                .collect {
                    currentPage += 1
                    usersList.addAll(it)
                    _users.value = LatestUiState.Success(usersList)
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