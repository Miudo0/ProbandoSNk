package com.empresa.snk.domain.titansDomain

import com.google.gson.annotations.SerializedName

data class TitansResponse(
    @SerializedName("infoTitan"    ) var info    : InfoTitan?              = InfoTitan(),
    @SerializedName("results" ) var results : ArrayList<Titan> = arrayListOf()
)