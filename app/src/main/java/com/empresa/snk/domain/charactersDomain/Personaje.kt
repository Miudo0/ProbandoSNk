package com.empresa.snk.domain.charactersDomain

import com.google.gson.annotations.SerializedName

data class Personaje (
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("img") var img: String? = null,
    @SerializedName("alias") var alias: List<String> = emptyList(),
    @SerializedName("species") var species: List<String> = emptyList(),
    @SerializedName("gender") var gender: String? = null,
    @SerializedName("age") var age: String? = null, // Cambiado a String?
    @SerializedName("height") var height: String? = null,
    @SerializedName("relatives") var relatives: List<Relatives> = emptyList(),
    @SerializedName("birthplace") var birthplace: String? = null,
    @SerializedName("residence") var residence: String? = null,
    @SerializedName("status") var status: String? = null,
    @SerializedName("occupation") var occupation: String? = null,
    @SerializedName("groups") var groups: List<Groups> = emptyList(),
    @SerializedName("roles") var roles: List<String> = emptyList(),
    @SerializedName("episodes") var episodes: List<String> = emptyList()
)