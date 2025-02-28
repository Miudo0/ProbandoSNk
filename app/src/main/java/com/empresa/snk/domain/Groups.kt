package com.empresa.snk.domain

import com.google.gson.annotations.SerializedName

data class Groups (
    @SerializedName("name"       ) var name      : String?           = null,
    @SerializedName("sub_groups" ) var subGroups : List<String> = emptyList()
)