package com.example.travelbucket

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Bucket::class], version = 1)
abstract class BucketsDB : RoomDatabase(){
    abstract fun BucketsDAO(): BucketsDAO

    companion object {
        // Singleton prevents multiple instances of database opening at the same time.
        @Volatile
        private var INSTANCE: BucketsDB? = null
        fun getInstance(context: Context): BucketsDB {
            // if the INSTANCE is not null, then return it, // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BucketsDB::class.java,
                    "buckets_database"
                )
                    .build()
                INSTANCE = instance // return instance
                instance
            }
        }
    }
}