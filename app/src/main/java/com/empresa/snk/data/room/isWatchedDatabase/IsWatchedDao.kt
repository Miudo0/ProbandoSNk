package com.empresa.snk.data.room.isWatchedDatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface IsWatchedDao {
    // Consultar si un episodio está marcado como visto por su ID
    @Query("SELECT is_watched FROM is_watched WHERE id = :episodeId")
    suspend fun isWatched(episodeId: Int): Boolean?

    // Insertar o actualizar el estado de un episodio
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(isWatched: IsWatchedEntity)

}