package com.empresa.snk.data.room.isWatchedDatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.empresa.snk.domain.IsWatched

@Entity(tableName = "is_watched")
class IsWatchedEntity (
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "is_watched") val isWatched: Boolean

)
fun IsWatchedEntity.toDomain() = IsWatched(id, isWatched)
fun IsWatched.toEntity() = IsWatchedEntity(id, isWatched)