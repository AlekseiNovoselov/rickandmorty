package com.lexaloris.rickandmorty.data.response

import com.google.gson.annotations.SerializedName

data class CharactersResponse(
    @SerializedName("info")
    val information: Information,
    @SerializedName("results")
    val characters: List<Character>
)
