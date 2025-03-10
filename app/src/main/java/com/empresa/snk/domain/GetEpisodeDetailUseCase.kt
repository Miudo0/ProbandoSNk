package com.empresa.snk.domain

import android.util.Log
import com.empresa.snk.data.repository.EpisodesRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetEpisodeDetailUseCase @Inject constructor(
    private val episodesRepository: EpisodesRepository
) {
    suspend operator fun invoke(pageUrl: String): String {
        return withContext(IO) {

            try {

                val episode = episodesRepository.getEpisodeByCharacter(pageUrl)
                Log.d("GetEpisodeDetailUseCase", "Episode: $episode")
                episode?.name ?: "No disponible"  // Si el nombre es nulo o el episodio no existe
            } catch (e: Exception) {

                "Error al cargar"
            }
        }
    }
}