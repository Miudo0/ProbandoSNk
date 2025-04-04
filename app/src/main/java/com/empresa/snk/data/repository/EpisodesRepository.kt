package com.empresa.snk.data.repository

import android.util.Log
import com.empresa.snk.data.network.SNKApi
import com.empresa.snk.domain.episodesDomain.Episodes
import com.empresa.snk.domain.episodesDomain.EpisodesResponse
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EpisodesRepository @Inject constructor(
    private val SNKApi: SNKApi
) {
    suspend fun getEpisodes(pageUrl: String?): EpisodesResponse {
        return withContext(IO){
            if (pageUrl == null) {
                SNKApi.getEpisodes()
            }
            else {
                SNKApi.getEpisodesByUrl(pageUrl)
            }
        }
    }

    //un episodio
    suspend fun getEpisodeByCharacter(episodeUrl: String): Episodes? {
        return withContext(IO) {
            try {
             SNKApi.getEpisodesByCharacterUrl(episodeUrl)

            } catch (e: Exception) {
                Log.e("EpisodesRepository", "Error al obtener el episodio: $episodeUrl", e)
               null
            }
        }
    }
    //episodos por Season
    suspend fun getEpisodesBySeason(season: String): EpisodesResponse{
//        return withContext(IO){
//            Log.d("EpisodesRepository", "Fetching episodes from API for season: $season")
//            val response = SNKApi.getEpisodesBySeason(season)
//            Log.d("EpisodesRepository", "Response: ${response.results.size} episodes found.")
//            response
//        }
        try {
            // Log para verificar que se pasa el season correctamente
            Log.d("EpisodesRepository", "Fetching episodes from API for season: $season")
            val response = SNKApi.getEpisodesBySeason(season)
            Log.d("EpisodesRepository", "Response: ${response.results.size} episodes found.")
            return response
        } catch (e: Exception) {
            Log.e("EpisodesRepository", "Error in getEpisodesBySeason: ${e.message}")
            throw e // Propagar el error para que se maneje adecuadamente
        }
    }


}