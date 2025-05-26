package com.empresa.snk.domain

import com.empresa.snk.data.repository.IsWatchedRepository
import javax.inject.Inject

class SetWatchedEpisodeUseCase @Inject constructor(
    private val repository: IsWatchedRepository
) {
    suspend operator fun invoke(episodeId: Int, isWatched: Boolean) {
        repository.setWatchedEpisode(episodeId, isWatched)

    }

}