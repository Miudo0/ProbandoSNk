package com.empresa.snk.data.network

import com.empresa.snk.domain.ApiResponse
import com.empresa.snk.domain.Info
import retrofit2.http.GET

//https://api.attackontitanapi.com/


interface SNKApi{
    @GET("characters")
    suspend fun getCharacters(): ApiResponse

}

