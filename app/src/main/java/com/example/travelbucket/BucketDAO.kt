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

    //delete specific bucket
    @Query("DELETE FROM bucket WHERE bucketId = :bucketId")
    fun deleteBucket(bucketId: Int)
    //delete events from deleted bucket
    @Query("DELETE FROM event WHERE bucketId = :bucketId")
    fun deleteEventsFromDeletedBucket(bucketId: Int)

    //delete specific event
    @Query("DELETE FROM event WHERE eventId = :eventId")
    fun deleteEvent(eventId: Int)

    //@Query("UPDATE event SET title=:title, costs=:costs, date=:date, notes=:notes, links=:links, duration=:duration WHERE eventId = :eventId")
    @Query("UPDATE event SET title=:title, costs=:costs, notes=:notes, links=:links, duration=:duration WHERE eventId = :eventId")
    fun updateEvent(
        eventId: Int,
        title: String,
        costs: Int,
        //date: Date,
        notes: String,
        links: String,
        duration: Int
    )

    //update specific bucket
    @Query("UPDATE bucket SET title=:title, description=:description WHERE bucketId = :bucketId")
    fun updateBucket(bucketId: Int, title: String, description: String)

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

    //get specific Event
    @Transaction
    @Query("SELECT * FROM event where eventId = :eventId")
    fun getEvent(eventId : Int): Event

    @Transaction
    @Query("SELECT * FROM bucket where bucketId = :bucketId")
    fun getBucket(bucketId: Int): Bucket

/*    //get event title
    @Query("SELECT title FROM event where eventId = :eventId")
    fun getEventTitle(eventId: Int): String*/
}