package com.example.github_ui.viewmodel

import androidx.lifecycle.*
import com.example.domain.usecases.CheckFavoriteStatusUseCase
import com.example.domain.usecases.DeleteFavoriteUseCase
import com.example.domain.usecases.FavoriteUserUseCase
import com.example.github_ui.mappers.GithubUsersModelMapper
import com.example.github_ui.models.GithubUsersModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val mapper: GithubUsersModelMapper,
    private val favoriteUserUseCase: FavoriteUserUseCase,
    private val deleteFavoritesUseCase: DeleteFavoriteUseCase,
    private val checkFavoritesUseCase: CheckFavoriteStatusUseCase,
) : ViewModel() {

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

    fun favoriteUser(user: GithubUsersModel) {
        viewModelScope.launch {
            if (_isFavorite.value == true) {
                deleteFavoritesUseCase(mapper.mapToDomain(user))
            } else {
                favoriteUserUseCase(mapper.mapToDomain(user))
            }
            _user.value = user
        }
    }

}