package com.empresa.snk.domain.organizationsDomain

import com.google.gson.annotations.SerializedName

data class Organization(

    @SerializedName("id"                     ) var id                   : Int?              = null,
    @SerializedName("name"                   ) var name                 : String?           = null,
    @SerializedName("img"                    ) var img                  : String?           = null,
    @SerializedName("occupations"            ) var occupations          : ArrayList<String> = arrayListOf(),
    @SerializedName("notable_members"        ) var notableMembers       : ArrayList<String> = arrayListOf(),
    @SerializedName("notable_former_members" ) var notableFormerMembers : ArrayList<String> = arrayListOf(),
    @SerializedName("affiliation"            ) var affiliation          : String?           = null,
    @SerializedName("debut"                  ) var debut                : String?           = null

)