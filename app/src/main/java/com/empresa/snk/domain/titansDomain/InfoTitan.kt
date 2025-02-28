package com.empresa.snk.domain.titansDomain

import com.google.gson.annotations.SerializedName

data class InfoTitan (
    @SerializedName("count"     ) var count    : Int?    = null,
    @SerializedName("pages"     ) var pages    : Int?    = null,
    @SerializedName("next_page" ) var nextPage : String? = null,
    @SerializedName("prev_page" ) var prevPage : String? = null
)