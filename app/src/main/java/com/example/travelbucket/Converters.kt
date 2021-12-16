package com.example.travelbucket

import androidx.room.TypeConverter
import java.util.*

class Converters {
    // convert long to date
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    // convert date to long
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }
}