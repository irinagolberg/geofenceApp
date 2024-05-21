package com.test.employeepresence.places.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction


@Dao
interface HoursDao {

    @Query("SELECT * FROM hours WHERE placeId = :placeIdKey ORDER BY timestamp ASC")
    suspend fun getHours(placeIdKey: String): List<HoursEntity>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHourRecord(record: HoursEntity)
}
