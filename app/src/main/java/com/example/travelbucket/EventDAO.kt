package com.example.travelbucket

import androidx.room.*

@Dao
interface EventsDAO {
    //insert bucket
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(Event: Event)

    //delete specific
    @Delete
    fun delete(Event: Event)

    //delete all
    @Query("DELETE FROM Event")
    fun nukeTable()

    //get all
    @Query("SELECT * FROM Event")
    fun getAll(): List<Event>
}