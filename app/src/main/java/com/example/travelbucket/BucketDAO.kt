package com.example.travelbucket

import androidx.room.*

@Dao
interface BucketsDAO {
    //insert bucket
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBucket(bucket: Bucket)

    //insert event
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEvent(event: Event)

    //delete specific
    @Delete
    fun delete(bucket: Bucket)

    //delete all
    @Query("DELETE FROM Bucket")
    fun nukeTable()

    //get all Buckets
    @Query("SELECT * FROM Bucket")
    fun getAllBuckets(): List<Bucket>

    //get all Events
    @Query("SELECT * FROM Event")
    fun getAllEvents(): List<Event>

    //get all Events for one Bucket
    @Transaction
    @Query("SELECT * FROM bucket where bucketId = :bucketId")
    fun getBucketWithEvents(bucketId : Int): List<BucketWithEvents>
}