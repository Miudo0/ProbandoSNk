package com.empresa.snk.data.network

import com.empresa.snk.domain.locationsDomain.LocationsResponse
import retrofit2.http.GET
import retrofit2.http.Url

interface SNKApiLocations {

    @GET("locations")
    suspend fun getLocations(): LocationsResponse


    @GET
    suspend fun getLocationsByUrl(@Url url: String): LocationsResponse

}