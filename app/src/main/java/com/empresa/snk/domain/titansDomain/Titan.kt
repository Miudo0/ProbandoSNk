package com.empresa.snk.domain.titansDomain

import com.google.gson.annotations.SerializedName

data class Titan (
    @SerializedName("id"                ) var id               : Int?              = null,
    @SerializedName("name"              ) var name             : String?           = null,
    @SerializedName("img"               ) var img              : String?           = null,
    @SerializedName("height"            ) var height           : String?           = null,
    @SerializedName("abilities"         ) var abilities        : ArrayList<String> = arrayListOf(),
    @SerializedName("current_inheritor" ) var currentInheritor : String?           = null,
    @SerializedName("former_inheritors" ) var formerInheritors : ArrayList<String> = arrayListOf(),
    @SerializedName("allegiance"        ) var allegiance       : String?           = null
)