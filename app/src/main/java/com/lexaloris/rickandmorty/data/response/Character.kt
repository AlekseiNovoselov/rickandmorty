package com.lexaloris.rickandmorty.data.response

import com.google.gson.annotations.SerializedName

data class Character(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("image")
    val imageUrl: String
)
