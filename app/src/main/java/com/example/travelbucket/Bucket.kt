package com.example.travelbucket
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Bucket")
data class Bucket(
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true) val id:Int,
    var title: String,
    //var image: Int
    )
