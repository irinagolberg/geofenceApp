package com.test.employeepresence.hours.data

import android.util.Log
import com.test.employeepresence.hours.domain.HoursRecord
import com.test.employeepresence.hours.domain.HoursRecordType
import com.test.employeepresence.places.data.db.HoursDao
import com.test.employeepresence.places.data.db.HoursEntity
import com.test.employeepresence.utils.APP_LOGTAG
import java.util.Date
import javax.inject.Inject

class HoursLocalDataSourceImpl @Inject constructor(private val hoursDao: HoursDao): HoursDataSource {
    override suspend fun saveHours(record: HoursRecord): Long {
        val entity = HoursEntity(
            timestamp = record.date.time,
            inside = record.type == HoursRecordType.ENTER,
            placeId = record.place?.cacheId
        )
        Log.d(APP_LOGTAG, "ds saveHours ${record.date} $entity")
        hoursDao.insertHourRecord(entity)
        return entity.id
    }

    override suspend fun getHours(placeId: Long?): List<HoursRecord> {
        return hoursDao.getHours(placeId).map {
            HoursRecord(
                date = Date(it.timestamp),
                type = if (it.inside) HoursRecordType.ENTER else HoursRecordType.EXIT
            )
        }
    }

    /*suspend fun loadPlace(): WorkingPlace? {
      / return placesDao.getPlaces().firstOrNull()?.let { place ->
           WorkingPlace(
               cacheId = place.id,
               latitude = place.latitude,
               longitude = place.longitude,
               address = place.address
           )
       }
   }*/
}