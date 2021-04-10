package com.example.github_ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.GithubUser
import com.example.domain.model.GithubUserResponse
import com.example.domain.usecases.FavoriteUserUseCase
import com.example.domain.usecases.GetFavoriteUsersUseCase
import com.example.domain.usecases.LoadMoreUsersUseCase
import com.example.domain.usecases.SearchUsersUseCase
import com.example.github_ui.mappers.GithubUsersModelMapper
import com.example.github_ui.models.GithubUsersModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
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

    private val _isLoadingMore = MutableLiveData<Boolean>()
    val isLoadingMore: LiveData<Boolean> = _isLoadingMore

    fun setQueryInfo(query: String) {
        queryItem = query
    }

    var count = 0

    fun searchGithubUsers() {
        _users.value = LatestUiState.Loading
        params = SearchUsersUseCase.Params(
            query = queryItem,
            pageNumber = 1
        )
        viewModelScope.launch {
            searchUsers(params)
                .combine(getFavoriteUseCase())
                { searchItems: GithubUserResponse, favoritesItem: List<GithubUser> ->
                    count = searchItems.total_count
                    searchItems.items.onEach { user ->
                        val favoriteItem = favoritesItem.find { it.id == user.id }
                        val exist = favoriteItem != null
                        user.isFavorite = exist
                    }
                }
                .map {
                    mapper.mapToModelList(it)
                }
                .catch {
                    _users.value = LatestUiState.Error("Cannot fetch users")
                }
                .collect {
                    usersList.clear()
                    usersList.addAll(it)
                    _users.value = LatestUiState.Success(usersList)
                }
        }
    }

    fun fetchMoreUsers() {
        if (shouldFetch()) {
            _isLoadingMore.value = true
        }
        params = SearchUsersUseCase.Params(
            query = queryItem,
            pageNumber = currentPage + 1
        )
        viewModelScope.launch {
            loadMore(params).combine(getFavoriteUseCase())
            { searchItems: GithubUserResponse, favoritesItem: List<GithubUser> ->
                searchItems.items.onEach { user ->
                    val favoriteItem = favoritesItem.find { it.id == user.id }
                    val exist = favoriteItem != null
                    user.isFavorite = exist
                }
            }
                .map {
                    mapper.mapToModelList(it)
                }
                .catch {
                    _users.value = LatestUiState.Error("Cannot fetch more users")
                }
                .collect {
                    _isLoadingMore.value = false
                    currentPage += 1
                    usersList.addAll(it)
                    _users.value = LatestUiState.Success(usersList)
                }
        }
    }

    fun shouldFetch() = usersList.size < count

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