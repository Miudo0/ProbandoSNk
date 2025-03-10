package com.empresa.snk.domain

import com.empresa.snk.data.repository.EpisodesRepository
import com.empresa.snk.domain.EpisodesDomain.EpisodesResponse
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetEpisodesUseCase @Inject constructor(
    private val episodesRepository: EpisodesRepository
) {
    suspend operator fun invoke(pageUrl: String?) : EpisodesResponse {
       return withContext(IO){
            episodesRepository.getEpisodes(pageUrl)
        }
    }
}