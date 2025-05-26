package com.empresa.snk.data.room.isWatchedDatabase

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [IsWatchedEntity::class], version = 1)
abstract class IsWatchedDatabase : RoomDatabase() {
    abstract fun isWatchedDao(): IsWatchedDao

}
