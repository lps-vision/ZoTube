package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        WatchHistoryEntity::class,
        BookmarkEntity::class,
        SearchHistoryEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class ZoTubeDatabase : RoomDatabase() {
    abstract fun zoTubeDao(): ZoTubeDao

    companion object {
        @Volatile
        private var INSTANCE: ZoTubeDatabase? = null

        fun getDatabase(context: Context): ZoTubeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ZoTubeDatabase::class.java,
                    "zotube_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
