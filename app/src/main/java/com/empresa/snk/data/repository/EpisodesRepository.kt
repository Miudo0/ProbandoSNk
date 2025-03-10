package com.empresa.snk.data.repository

import android.util.Log
import com.empresa.snk.data.network.SNKApi
import com.empresa.snk.domain.EpisodesDomain.Episodes
import com.empresa.snk.domain.EpisodesDomain.EpisodesResponse
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

}