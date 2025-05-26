package com.empresa.snk.domain

import com.empresa.snk.data.repository.IsWatchedRepository
import javax.inject.Inject

class GetIsWatchedUseCase @Inject constructor(
private val repository: IsWatchedRepository
) {

    suspend operator fun invoke(episodeId: Int): Boolean {
        return repository.IsWatchedEpisode(episodeId)
    }
}