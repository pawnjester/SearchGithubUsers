package com.example.github_ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.domain.model.GithubUser
import com.example.domain.usecases.DeleteFavoriteUseCase
import com.example.domain.usecases.FavoriteUserUseCase
import com.example.domain.usecases.GetFavoriteUsersUseCase
import com.example.github_ui.MainCoroutineRule
import com.example.github_ui.mappers.DummyData.makeGithubUser
import com.example.github_ui.mappers.DummyData.makeGithubUserList
import com.example.github_ui.mappers.DummyData.makeGithubUserModel
import com.example.github_ui.mappers.GithubUsersModelMapper
import com.example.github_ui.models.GithubUsersModel
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.times
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.*

class FavoriteUserViewModelTest {


    private lateinit var sut: FavoriteUserViewModel

    @Mock
    lateinit var getUsers: GetFavoriteUsersUseCase


    @Mock
    lateinit var favoriteUserUseCase: FavoriteUserUseCase

    @Mock
    lateinit var deleteFavoritesUseCase: DeleteFavoriteUseCase

    @Mock
    lateinit var mapper: GithubUsersModelMapper

    @Mock
    lateinit var uiObserver: Observer<LatestUiState<List<GithubUsersModel>>>

    @Captor
    private lateinit var captorUI: ArgumentCaptor<LatestUiState<List<GithubUsersModel>>>

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()


    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        sut = FavoriteUserViewModel(
            getUsers, favoriteUserUseCase, deleteFavoritesUseCase, mapper
        )
    }


    @Test
    fun `test to confirm a user is favorited`() = runBlocking {
        sut.favoriteUsers.observeForever(uiObserver)

        val users = makeGithubUser()
        val usersModel = makeGithubUserModel()

        Mockito.`when`(favoriteUserUseCase(users)).thenReturn(Unit)

        sut.favoriteUser(usersModel, false)
        assertThat(usersModel.isFavorite).isEqualTo(false)
    }

    @Test
    fun `test to get favorite users`() = runBlocking {
        sut.favoriteUsers.observeForever(uiObserver)

        val users: List<GithubUser> = makeGithubUserList(1)

        Mockito.`when`(getUsers()).thenReturn(
            flow {
                emit(users)
            }
        )
        sut.getFavoriteUsers()
        Mockito.verify(uiObserver, times(1)).onChanged(captorUI.capture())
        sut.favoriteUsers.removeObserver(uiObserver)
    }
}