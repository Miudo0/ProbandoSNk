package com.empresa.snk.data.network


import com.empresa.snk.domain.charactersDomain.CharactersResponse
import com.empresa.snk.domain.titansDomain.TitansResponse

import retrofit2.http.GET
import retrofit2.http.Url

//https://api.attackontitanapi.com/


interface SNKApi{
    @GET("characters")
    suspend fun getCharacters(): CharactersResponse

    @GET
    suspend fun getCharactersByUrl(@Url pageUrl: String): CharactersResponse

    @GET("titans")
    suspend fun getTitans(): TitansResponse


    @GET
    suspend fun getTitansByUrl(@Url pageUrl: String): TitansResponse

}

