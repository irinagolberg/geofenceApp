package com.test.employeepresence.places.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [PlacesEntity::class], version = 1, exportSchema = false)
abstract class PlacesDatabase : RoomDatabase() {
    abstract fun placesDao(): PlacesDao
}