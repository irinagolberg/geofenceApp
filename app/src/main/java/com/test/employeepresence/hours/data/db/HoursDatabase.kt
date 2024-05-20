package com.test.employeepresence.places.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [HoursEntity::class], version = 1, exportSchema = false)
abstract class HoursDatabase : RoomDatabase() {
    abstract fun hoursDao(): HoursDao
}