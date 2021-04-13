package com.example.github_ui.viewmodel

import androidx.lifecycle.*
import com.example.domain.usecases.*
import com.example.github_ui.mappers.GithubUsersModelMapper
import com.example.github_ui.models.GithubUsersModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val searchUsers: SearchUsersUseCase,
    private val loadMore: LoadMoreUsersUseCase,
    private val favoriteUserUseCase: FavoriteUserUseCase,
    private val deleteFavoritesUseCase: DeleteFavoriteUseCase,
    private val mapper: GithubUsersModelMapper,
    private val savedStateHandle: SavedStateHandle,
    private val checkFavoritesUseCase: CheckFavoriteStatusUseCase,
) : ViewModel() {

    private val _users = MutableLiveData<LatestUiState<List<GithubUsersModel>>>()
    val users: LiveData<LatestUiState<List<GithubUsersModel>>> = _users

    private val usersList = mutableListOf<GithubUsersModel>()

    private var params: SearchUsersUseCase.Params? = null

    private var currentPage: Int = 1

    private val _isLoadingMore = MutableLiveData<Boolean>()
    val isLoadingMore: LiveData<Boolean> = _isLoadingMore

    private var count = 0

    private var lastQuery: String? = null

    private val queryValue: MutableLiveData<String> =
        savedStateHandle.getLiveData("query")

    private var _user = MutableLiveData<GithubUsersModel>()
    var user: LiveData<GithubUsersModel> = _user

    private val _isFavorite: LiveData<Boolean> = Transformations.switchMap(_user) {
        checkFavoritesUseCase(it.id).asLiveData()
    }
    val isFavorite: LiveData<Boolean> = Transformations.map(_isFavorite) { it }

    fun setUserDetail(user: GithubUsersModel?) {
        user?.let {
            _user.value = it
        }
    }

    fun favoriteUserDetail(user: GithubUsersModel) {
        viewModelScope.launch {
            if (_isFavorite.value == true) {
                deleteFavoritesUseCase(mapper.mapToDomain(user))
            } else {
                favoriteUserUseCase(mapper.mapToDomain(user))
            }
            _user.value = user
            toggleSelectedItem(user)
        }
    }

    fun setQueryInfo(query: String) {
        savedStateHandle["query"] = query
        if (query == lastQuery) return

        query.also {
            lastQuery = it
            queryValue.value = it
        }
        searchGithubUsers()
    }

    fun searchGithubUsers() {
        if (lastQuery.isNullOrEmpty()) return

        _users.value = LatestUiState.Loading
        currentPage = 1
        params = SearchUsersUseCase.Params(
            query = lastQuery ?: "",
            pageNumber = currentPage
        )
        viewModelScope.launch {
            searchUsers(params)
                .map {
                    count = it.total_count
                    mapper.mapToModelList(it.items)
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
            query = lastQuery ?: "",
            pageNumber = currentPage + 1
        )
        viewModelScope.launch {
            loadMore(params)
                .map {
                    mapper.mapToModelList(it.items)
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

    private fun shouldFetch() = usersList.size < count

    private fun toggleSelectedItem(item: GithubUsersModel) {
        usersList.find {
            it.id == item.id
        }?.isFavorite = !item.isFavorite
        _users.value = LatestUiState.Success(usersList)
    }

    fun favoriteUser(user: GithubUsersModel, isFavorite: Boolean) {
        viewModelScope.launch {
            if (isFavorite) {
                favoriteUserUseCase(mapper.mapToDomain(user))
            } else {
                deleteFavoritesUseCase(mapper.mapToDomain(user))
            }
        }
    }
}

sealed class LatestUiState<out T : Any> {
    data class Success<out T : Any>(val users: T) : LatestUiState<T>()
    object Loading : LatestUiState<Nothing>()
    data class Error(val exception: String) : LatestUiState<Nothing>()
}