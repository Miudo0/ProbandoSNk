package com.empresa.snk.data.network


import com.empresa.snk.domain.charactersDomain.CharactersResponse
import com.empresa.snk.domain.titansDomain.TitansResponse

import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

//https://api.attackontitanapi.com/


interface SNKApi{
    //characters
    @GET("characters")
    suspend fun getCharacters(): CharactersResponse

    @GET
    suspend fun getCharactersByUrl(@Url pageUrl: String): CharactersResponse

    @GET("characters")
    suspend fun getFilteredCharacters(
        @Query("name") name: String? = null,
        @Query("gender") gender: String? = null,
        @Query("status") status: String? = null,
        @Query("occupation") occupation: String? = null
    ): CharactersResponse

    //titans
    @GET("titans")
    suspend fun getTitans(): TitansResponse


    @GET
    suspend fun getTitansByUrl(@Url pageUrl: String): TitansResponse

}

