package com.example.travelbucket

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Event")
data class Event(
    @PrimaryKey(autoGenerate = true) val id:Int,
    var title: String,
    var costs: String,
    var date: String,
    var location: String,
    var notes: String,
    var links: String,
    var duration: String)


