package com.empresa.snk.data.network


import com.empresa.snk.domain.charactersDomain.CharactersResponse
import com.empresa.snk.domain.charactersDomain.Personaje
import com.empresa.snk.domain.episodesDomain.Episodes
import com.empresa.snk.domain.episodesDomain.EpisodesResponse
import com.empresa.snk.domain.titansDomain.TitansResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

//https://api.attackontitanapi.com/

//esto luego separarlo todo en modulos
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

    @GET
    suspend fun getFamilyMembers(@Url familyUrl: String): Personaje


    //titans
    @GET("titans")
    suspend fun getTitans(): TitansResponse


    @GET
    suspend fun getTitansByUrl(@Url pageUrl: String): TitansResponse

    @GET
    suspend fun getCurrentInheritor(@Url currentInheritorUrl: String): Personaje

    @GET
    suspend fun getFormerInheritors(@Url formerInheritorUrl: String): Personaje


    //Episodes
    @GET("episodes")
    suspend fun getEpisodes(): EpisodesResponse

    @GET
    suspend fun getEpisodesByUrl(@Url episodeUrl: String): EpisodesResponse

    @GET
    suspend fun  getEpisodesByCharacterUrl(@Url characterUrl: String): Episodes

    @GET
    suspend fun getEpisodesBySeason(@Query("episode") season: String): EpisodesResponse
}



