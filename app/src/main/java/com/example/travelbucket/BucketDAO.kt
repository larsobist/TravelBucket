package com.example.travelbucket

import androidx.room.*

@Dao
interface BucketsDAO {
    //insert bucket
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(Bucket: Bucket)

    //delete specific
    @Delete
    fun delete(Bucket: Bucket)

    //delete all
    @Query("DELETE FROM Bucket")
    fun nukeTable()

    //get all
    @Query("SELECT * FROM Bucket")
    fun getAll(): List<Bucket>
}