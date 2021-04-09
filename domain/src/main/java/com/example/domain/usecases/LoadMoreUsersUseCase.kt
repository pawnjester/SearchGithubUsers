package com.example.domain.usecases

import com.example.domain.executor.PostExecutorThread
import com.example.domain.model.GithubUser
import com.example.domain.repositories.GithubUsersRepository
import com.example.domain.usecases.base.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoadMoreUsersUseCase @Inject constructor(
    private val repository: GithubUsersRepository,
    private val postExecution: PostExecutorThread
) : FlowUseCase<SearchUsersUseCase.Params, List<GithubUser>>() {

    override val dispatcher: CoroutineDispatcher
        get() = postExecution.io

    override fun execute(params: SearchUsersUseCase.Params?): Flow<List<GithubUser>> {
        requireNotNull(params) { "params cannot be null" }
        return repository.searchUsers(params.query, params.pageNumber)
    }
}