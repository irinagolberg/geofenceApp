package com.test.employeepresence.places.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

const val PLACES_TABLE_NAME = "places"
@Entity(tableName = PLACES_TABLE_NAME)
data class PlacesEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val latitude: Double,
    val longitude: Double,
    val address: String?
)