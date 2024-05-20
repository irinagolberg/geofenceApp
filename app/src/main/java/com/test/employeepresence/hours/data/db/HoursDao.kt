package com.test.employeepresence.places.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction


@Dao
interface HoursDao {
    @Transaction
    suspend fun saveHoursRecord(entity: HoursEntity): Long {
        /*val listToDelete = getHoursItems(entity.latitude, entity.longitude, entity.address)
        deletePlaces(listToDelete)
        val savedEntity =
            listToDelete.firstOrNull()?.id?.let { entity.copy(id = it) } ?: entity
        insertLocation(savedEntity)
        return savedEntity.id*/
        return -1
    }

    @Query("SELECT * FROM hours WHERE (placeId = :placeId OR placeId is NULL) ORDER BY timestamp DESC")
    suspend fun getHours(placeId: Long?): List<HoursEntity>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHourRecord(record: HoursEntity)

   /* @Delete
    suspend fun deletePlaces(places: List<PlacesEntity>)
    @Query("SELECT * FROM places WHERE ((latitude = :latitude AND longitude = :longitude) OR (address = :address))")
    suspend fun getPlacesItems(
        latitude: Double,
        longitude: Double,
        address: String?
    ): List<PlacesEntity>

    @Query("SELECT * FROM places")
    suspend fun getPlaces(): List<PlacesEntity>*/
}
