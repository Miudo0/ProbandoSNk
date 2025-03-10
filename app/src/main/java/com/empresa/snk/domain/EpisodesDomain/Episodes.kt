package com.empresa.snk.domain.EpisodesDomain

import com.google.gson.annotations.SerializedName

data class Episodes (

    @SerializedName("id"         ) var id         : Int?              = null,
    @SerializedName("name"       ) var name       : String?           = null,
    @SerializedName("img"        ) var img        : String?           = null,
    @SerializedName("episode"    ) var episode    : String?           = null,
    @SerializedName("characters" ) var characters : ArrayList<String> = arrayListOf()

)