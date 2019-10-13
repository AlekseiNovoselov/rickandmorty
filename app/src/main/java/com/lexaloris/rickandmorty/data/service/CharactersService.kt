package com.lexaloris.rickandmorty.data.service

import com.lexaloris.rickandmorty.data.response.CharactersResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface CharactersService {

    @GET("character")
    fun getCharactersAsync(@Query("page") page: Int): Deferred<CharactersResponse>
}
