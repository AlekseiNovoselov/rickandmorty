package com.lexaloris.rickandmorty.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations.switchMap
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.lexaloris.rickandmorty.domain.interactor.GetCharactersInteractor
import com.lexaloris.rickandmorty.domain.model.NetworkState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import javax.inject.Inject

class CharactersViewModel
@Inject constructor(getCharactersInteractor: GetCharactersInteractor) : ViewModel() {
    private val ioScope = CoroutineScope(Dispatchers.Default)
    private val charactersDataSource = CharactersDataSourceFactory(
        getCharactersInteractor = getCharactersInteractor,
        scope = ioScope
    )
    val characters = LivePagedListBuilder(charactersDataSource, pagedListConfig()).build()
    val networkState: LiveData<NetworkState> = switchMap(charactersDataSource.source) { charactersDataSource ->
        charactersDataSource.getNetworkState()
    }

    fun refreshAllList() {
        charactersDataSource.getSource()?.refresh()
    }

    fun refreshFailedRequest() {
        charactersDataSource.getSource()?.retryFailedQuery()
    }

    private fun pagedListConfig(): PagedList.Config {
        return PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(20)
            .build()
    }

    override fun onCleared() {
        super.onCleared()
        ioScope.coroutineContext.cancel()
    }
}
