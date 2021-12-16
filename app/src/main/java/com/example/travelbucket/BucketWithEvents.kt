package com.example.travelbucket

import androidx.room.Embedded
import androidx.room.Relation

data class BucketWithEvents(
    @Embedded val bucket: Bucket,
    @Relation(
        parentColumn = "bucketId",
        entityColumn = "bucketId"
    )
    val events : List<Event>
)
