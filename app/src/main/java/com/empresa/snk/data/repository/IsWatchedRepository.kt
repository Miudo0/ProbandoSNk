package com.empresa.snk.data.repository


import com.empresa.snk.data.room.isWatchedDatabase.IsWatchedDatabase
import com.empresa.snk.data.room.isWatchedDatabase.IsWatchedEntity
import javax.inject.Inject

class IsWatchedRepository @Inject constructor(
    private val isWatchedDatabase: IsWatchedDatabase
) {
    suspend fun IsWatchedEpisode(episodeId: Int): Boolean {
     return  isWatchedDatabase.isWatchedDao().isWatched(episodeId) ?: false
    }
    suspend fun setWatchedEpisode(episodeId: Int, isWatched: Boolean) {
        val entity = IsWatchedEntity(id = episodeId, isWatched = isWatched)
        isWatchedDatabase.isWatchedDao().insert(entity)
    }

}