package com.example.travelbucket

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "Event",
    foreignKeys = arrayOf(ForeignKey(entity = Bucket::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("id"),
    onDelete = ForeignKey.CASCADE)))

data class Event(
    //val foreignKey: Int,
    @PrimaryKey(autoGenerate = true) val id:Int,
    var title: String,
    var costs: String,
    var date: String,
    var location: String,
    var notes: String,
    var links: String,
    var duration: String)


