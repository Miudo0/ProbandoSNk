package com.empresa.snk.domain.charactersDomain

import com.google.gson.annotations.SerializedName

data class Info (
    @SerializedName("count"     ) var count    : Int?    = null,
    @SerializedName("pages"     ) var pages    : Int?    = null,
    @SerializedName("next_page" ) var nextPage : String? = null,
    @SerializedName("prev_page" ) var prevPage : String? = null
)