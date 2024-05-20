package com.test.employeepresence.hours.data

import android.content.SharedPreferences
import android.util.Log
import com.test.employeepresence.hours.domain.HoursRecord
import com.test.employeepresence.hours.domain.HoursRecordType
import com.test.employeepresence.hours.domain.HoursRepository
import com.test.employeepresence.places.data.PlacesDataSource
import com.test.employeepresence.utils.APP_LOGTAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

class HoursRepositoryImpl @Inject constructor(val hoursDataSource: HoursDataSource): HoursRepository {
    private val repositoryScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    override suspend fun saveHours(hours: HoursRecord) {
        Log.d(APP_LOGTAG, "repo saveHours $hours $hoursDataSource")
        repositoryScope.launch {
            hoursDataSource.saveHours(hours)
            hoursDataSource.saveHours(
                hours.copy(
                    type = HoursRecordType.EXIT,
                    date = addTimeToDate(hours.date, 0, 2)
                )
            )

            hoursDataSource.saveHours(hours.copy( date = addTimeToDate(hours.date, 2, 0)))
            hoursDataSource.saveHours(
                hours.copy(
                    type = HoursRecordType.EXIT,
                    date = addTimeToDate(hours.date, 2, 1)
                )
            )
        }
    }

    private fun addTimeToDate(date: Date, days: Int, hours: Int): Date {
        return Date(date.time + (24 * days + hours) * 60 * 60 * 1000)
    }
    override suspend fun getHours(): List<HoursRecord> {
        return hoursDataSource.getHours(null)
    }
}