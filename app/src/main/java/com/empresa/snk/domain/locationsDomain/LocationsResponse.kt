package com.empresa.snk.domain.locationsDomain

import com.google.gson.annotations.SerializedName

data class LocationsResponse(
    @SerializedName("info") var info: InfoLocations? = InfoLocations(),
    @SerializedName("results") var results: ArrayList<Location> = arrayListOf()
)