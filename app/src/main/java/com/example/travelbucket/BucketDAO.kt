package com.example.travelbucket

import androidx.room.*
import java.util.*

@Dao
interface BucketsDAO {
    //insert bucket
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBucket(bucket: Bucket)

    //insert event
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEvent(event: Event)

    //@Query("UPDATE event SET title=:title, costs=:costs, date=:date, location=:location, notes=:notes, links=:links, duration=:duration WHERE eventId = :eventId")
    @Query("UPDATE event SET title=:title, costs=:costs, location=:location, notes=:notes, links=:links, duration=:duration WHERE eventId = :eventId")
    fun updateEvent(
        eventId: Int,
        title: String,
        costs: String,
        //date: Date,
        location: String,
        notes: String,
        links: String,
        duration: String
    )

    //delete all
    @Query("DELETE FROM Bucket")
    fun nukeTable()

    //get all Buckets
    @Query("SELECT * FROM Bucket")
    fun getAllBuckets(): List<Bucket>

    //get all Buckets
    @Query("SELECT title FROM Bucket where bucketId = :bucketId")
    fun getBucketTitle(bucketId: Int): String

    //get all Events
    @Query("SELECT * FROM Event")
    fun getAllEvents(): List<Event>

    //get all Events for one Bucket
    @Transaction
    @Query("SELECT * FROM event where bucketId = :bucketId")
    fun getEventsOfBucket(bucketId : Int): List<Event>

    //get all Events for one Bucket
    @Transaction
    @Query("SELECT * FROM event where eventId = :eventId")
    fun getEvent(eventId : Int): Event
}