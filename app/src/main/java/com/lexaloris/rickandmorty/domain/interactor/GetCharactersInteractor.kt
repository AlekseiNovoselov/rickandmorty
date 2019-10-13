package com.lexaloris.rickandmorty.domain.interactor

import com.lexaloris.rickandmorty.data.response.CharactersResponse
import com.lexaloris.rickandmorty.data.service.CharactersService
import javax.inject.Inject

class GetCharactersInteractor
@Inject constructor(private val service: CharactersService) {

    suspend fun loadCharacters(page: Int): CharactersResponse {
        return service.getCharactersAsync(page).await()
    }
}
