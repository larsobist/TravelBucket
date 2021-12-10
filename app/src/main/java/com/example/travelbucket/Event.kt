package com.example.travelbucket

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.*

@Entity
@TypeConverters(Converters::class)
data class Event(
    @PrimaryKey(autoGenerate = true) val eventId:Int,
    val bucketId : Int,
    var title: String,
    var costs: String,
    var date: Date,
    var location: String,
    var notes: String,
    var links: String,
    var duration: String
    )


