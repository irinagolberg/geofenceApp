package com.test.employeepresence.places.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

const val HOURS_TABLE_NAME = "hours"
@Entity(tableName = HOURS_TABLE_NAME)
data class HoursEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestamp: Long,
    val inside: Boolean,
    val placeId: String
)