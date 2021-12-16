package com.example.travelbucket
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Bucket(
    @PrimaryKey(autoGenerate = true) val bucketId:Int,
    var title: String,
    var description: String,
    var color: String
    )
