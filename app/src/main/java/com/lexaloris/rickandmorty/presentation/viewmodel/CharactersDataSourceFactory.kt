package com.lexaloris.rickandmorty.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.lexaloris.rickandmorty.data.response.Character
import com.lexaloris.rickandmorty.domain.interactor.GetCharactersInteractor
import kotlinx.coroutines.CoroutineScope

class CharactersDataSourceFactory(
    private val getCharactersInteractor: GetCharactersInteractor,
    private val scope: CoroutineScope
) : DataSource.Factory<Int, Character>() {

    val source = MutableLiveData<CharactersDataSource>()

    override fun create(): DataSource<Int, Character> {
        val source = CharactersDataSource(getCharactersInteractor, scope)
        this.source.postValue(source)
        return source
    }

    fun getSource() = source.value
}
