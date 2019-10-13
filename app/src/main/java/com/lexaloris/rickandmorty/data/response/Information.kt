package com.lexaloris.rickandmorty.data.response

import com.google.gson.annotations.SerializedName

data class Information(
    @SerializedName("next")
    val nextPagePath: String
)
