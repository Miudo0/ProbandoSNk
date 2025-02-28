package com.empresa.snk.domain

import com.google.gson.annotations.SerializedName

data class ApiResponse (
    @SerializedName("info") val info: Info?,
    @SerializedName("results") val results: List<Characters>?
)