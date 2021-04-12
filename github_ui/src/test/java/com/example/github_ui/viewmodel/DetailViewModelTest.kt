package com.example.github_ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.domain.usecases.CheckFavoriteStatusUseCase
import com.example.domain.usecases.DeleteFavoriteUseCase
import com.example.domain.usecases.FavoriteUserUseCase
import com.example.github_ui.MainCoroutineRule
import com.example.github_ui.mappers.DummyData.makeGithubUserModel
import com.example.github_ui.mappers.GithubUsersModelMapper
import com.example.github_ui.models.GithubUsersModel
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.times
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.*

class DetailViewModelTest {

    private lateinit var sut: DetailViewModel

    @Mock
    lateinit var favoriteUserUseCase: FavoriteUserUseCase

    @Mock
    lateinit var deleteFavoritesUseCase: DeleteFavoriteUseCase

    @Mock
    lateinit var checkFavoritesUseCase: CheckFavoriteStatusUseCase

    @Mock
    lateinit var mapper: GithubUsersModelMapper

    @Mock
    lateinit var uiObserver: Observer<GithubUsersModel>

    @Captor
    private lateinit var captorUI: ArgumentCaptor<GithubUsersModel>

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        sut = DetailViewModel(mapper, favoriteUserUseCase, deleteFavoritesUseCase, checkFavoritesUseCase)
    }

    @Test
    fun `set user details`() {
        sut.user.observeForever(uiObserver)

        val model = makeGithubUserModel()

        sut.setUserDetail(model)
        Mockito.verify(uiObserver, times(1)).onChanged(captorUI.capture())

        assertThat(captorUI.value).isEqualTo(model)
    }

    @Test
    fun `favorite user`() {
        sut.user.observeForever(uiObserver)

        val model = makeGithubUserModel()

        sut.favoriteUser(model)
        Mockito.verify(uiObserver, times(1)).onChanged(captorUI.capture())

        assertThat(captorUI.value).isEqualTo(model)
    }
}