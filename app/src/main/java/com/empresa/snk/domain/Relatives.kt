package com.empresa.snk.domain

import com.google.gson.annotations.SerializedName

data class Relatives (
    @SerializedName("family"  ) var family  : String?           = null,
    @SerializedName("members" ) var members : List<String> =  emptyList()
)
