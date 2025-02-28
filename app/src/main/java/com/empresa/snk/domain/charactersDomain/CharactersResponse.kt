package com.empresa.snk.domain.charactersDomain

import com.google.gson.annotations.SerializedName

data class CharactersResponse (
    @SerializedName("info") val info: Info?,
    @SerializedName("results") val results: List<Characters>?
)