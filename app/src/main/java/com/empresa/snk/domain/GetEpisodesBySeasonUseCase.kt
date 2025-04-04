package com.empresa.snk.domain

import android.util.Log
import com.empresa.snk.data.repository.EpisodesRepository
import com.empresa.snk.domain.episodesDomain.EpisodesResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetEpisodesBySeasonUseCase @Inject constructor(
    private val repository: EpisodesRepository
) {
    suspend operator fun invoke(season: String): EpisodesResponse {
        return withContext(Dispatchers.IO) {
            Log.d("GetEpisodesBySeasonUseCase", "Fetching episodes for season: $season")
            repository.getEpisodesBySeason(season)
        }
    }
}