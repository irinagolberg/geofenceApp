package com.test.employeepresence.places.data

import android.content.Context
import android.content.SharedPreferences
import com.test.employeepresence.places.domain.WorkingPlace
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PlacesLocalDataSourceImpl @Inject constructor(@ApplicationContext private val context: Context):
    PlacesDataSource {
    companion object {
        const val PREF_WORKING_PLACE = "WorkingPlace"
        private const val KEY_PLACE = "place"
    }
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_WORKING_PLACE, Context.MODE_PRIVATE)

    override suspend fun savePlace(place: WorkingPlace) {
        sharedPreferences.edit().apply {
            putString(
                KEY_PLACE,
                place.toPlaceIdKey()
            )
        }.apply()
    }

    override suspend fun loadPlace(): WorkingPlace? {
        val placeKey = sharedPreferences.getString(KEY_PLACE, null)
        return placeKey?.let { WorkingPlace.parseWorkingPlace(it) }
    }
}