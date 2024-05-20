package com.test.employeepresence.places.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction


@Dao
interface PlacesDao {
    @Transaction
    suspend fun saveLocation(entity: PlacesEntity): Long {
        val listToDelete = getPlacesItems(entity.latitude, entity.longitude, entity.address)
        deletePlaces(listToDelete)
        val savedEntity =
            listToDelete.firstOrNull()?.id?.let { entity.copy(id = it) } ?: entity
        insertLocation(savedEntity)
        return savedEntity.id
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(location: PlacesEntity)

    @Delete
    suspend fun deletePlaces(places: List<PlacesEntity>)
    @Query("SELECT * FROM places WHERE ((latitude = :latitude AND longitude = :longitude) OR (address = :address))")
    suspend fun getPlacesItems(
        latitude: Double,
        longitude: Double,
        address: String?
    ): List<PlacesEntity>

    @Query("SELECT * FROM places")
    suspend fun getPlaces(): List<PlacesEntity>
/*
    @Query("DELETE FROM locations WHERE ((latitude = :latitude AND longitude = :longitude) OR (address = :address))")
    suspend fun deleteLocationsByAddress(latitude: Double, longitude: Double, address: String?)


    @Query("SELECT * FROM locations WHERE (latitude = :latitude AND longitude = :longitude)")
    suspend fun getLocationItemsByCoordinates(
        latitude: Double,
        longitude: Double
    ): List<LocationEntity>

    @Query("SELECT * FROM locations WHERE address = :address")
    suspend fun getLocationItemsByAddress(address: String): List<LocationEntity>*/
}
