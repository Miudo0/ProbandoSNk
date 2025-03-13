package com.empresa.snk.domain.LocationsDomain

import com.google.gson.annotations.SerializedName

data class Location (
    @SerializedName("id"                         ) var id                       : Int?              = null,
    @SerializedName("name"                       ) var name                     : String?           = null,
    @SerializedName("img"                        ) var img                      : String?           = null,
    @SerializedName("territory"                  ) var territory                : String?           = null,
    @SerializedName("region"                     ) var region                   : String?           = null,
    @SerializedName("notable_inhabitants"        ) var notableInhabitants       : ArrayList<String> = arrayListOf(),
    @SerializedName("notable_former_inhabitants" ) var notableFormerInhabitants : ArrayList<String> = arrayListOf(),
    @SerializedName("debut"                      ) var debut                    : String?           = null

)