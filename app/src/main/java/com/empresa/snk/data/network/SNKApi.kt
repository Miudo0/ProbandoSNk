package com.empresa.snk.data.network

import com.empresa.snk.domain.ApiResponse
import com.empresa.snk.domain.Info
import retrofit2.http.GET
import retrofit2.http.Url

//https://api.attackontitanapi.com/


interface SNKApi{
    @GET("characters")
    suspend fun getCharacters(): ApiResponse

    @GET
    suspend fun getCharactersByUrl(@Url pageUrl: String): ApiResponse

}

