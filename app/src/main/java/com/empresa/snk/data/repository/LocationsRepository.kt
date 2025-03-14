package com.empresa.snk.data.repository

import com.empresa.snk.data.network.SNKApiLocations
import com.empresa.snk.domain.locationsDomain.LocationsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocationsRepository @Inject constructor(
    private val api: SNKApiLocations
){
    suspend fun getAllLocations(pageUrl: String? ): LocationsResponse{
        return withContext(Dispatchers.IO){
            if(pageUrl == null){
                api.getLocations()
            }else{
                api.getLocationsByUrl(pageUrl)
            }
        }
    }
}