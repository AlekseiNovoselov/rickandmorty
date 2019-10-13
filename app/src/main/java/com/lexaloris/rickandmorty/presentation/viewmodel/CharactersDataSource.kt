package com.lexaloris.rickandmorty.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.lexaloris.rickandmorty.data.response.Character
import com.lexaloris.rickandmorty.data.response.CharactersResponse
import com.lexaloris.rickandmorty.domain.interactor.GetCharactersInteractor
import com.lexaloris.rickandmorty.domain.model.NetworkState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch

class CharactersDataSource(
    private val getCharactersInteractor: GetCharactersInteractor,
    private val scope: CoroutineScope
) : PageKeyedDataSource<Int, Character>() {
    private val supervisorJob = SupervisorJob()
    private val networkState = MutableLiveData<NetworkState>()
    private var retryQuery: (() -> Unit)? = null

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Character>) {
        retryQuery = { loadInitial(params, callback) }
        val startPage = 1
        val nextPageKey = startPage + 1
        executeQuery(startPage) { response ->
            val data = response.characters
            val previousPageKey = null
            callback.onResult(data, previousPageKey, nextPageKey)
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Character>) {
        val currentPage = params.key
        retryQuery = { loadAfter(params, callback) }
        executeQuery(currentPage) { response ->
            val data = response.characters
            val nextPageKey = (currentPage + 1).takeIf { response.information.nextPagePath.isNotEmpty() }
            callback.onResult(data, nextPageKey)
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Character>) {
    }

    private fun executeQuery(page: Int, callback: (CharactersResponse) -> Unit) {
        networkState.postValue(NetworkState.RUNNING)
        scope.launch(context = createJobErrorHandler() + supervisorJob) {
            val users = getCharactersInteractor.loadCharacters(page)
            retryQuery = null
            networkState.postValue(NetworkState.SUCCESS)
            callback(users)
        }
    }

    private fun createJobErrorHandler(): CoroutineExceptionHandler {
        return CoroutineExceptionHandler { _, _ ->
            networkState.postValue(NetworkState.FAILED)
        }
    }

    fun getNetworkState(): LiveData<NetworkState> = networkState

    fun refresh() = invalidate()

    fun retryFailedQuery() {
        val prevQuery = retryQuery
        retryQuery = null
        prevQuery?.invoke()
    }

    override fun invalidate() {
        super.invalidate()
        supervisorJob.cancelChildren()
    }
}

