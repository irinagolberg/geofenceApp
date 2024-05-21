package com.test.employeepresence.hours.domain

import android.util.Log
import com.test.employeepresence.utils.APP_LOGTAG
import javax.inject.Inject

class HoursInteractor @Inject constructor(private val hoursRepository: HoursRepository) {
    suspend fun getHours(): List<DayRecord> {
        val dayRecords = mutableListOf<DayRecord>()
        val hours = hoursRepository.getHours()
        Log.d(APP_LOGTAG, "getHours hours: $hours")
        var entrance: HoursRecord? = null
        var exit: HoursRecord? = null
        hours.forEach { record ->
            when (record.type) {
                HoursRecordType.ENTER -> {
                    entrance = record
                }
                HoursRecordType.EXIT -> {
                    exit = record
                }
            }
            entrance?.let { entranceChecked ->
                exit?.let { exitChecked ->
                    if (entranceChecked.date.before(exitChecked.date)) {
                        dayRecords.add(
                            DayRecord(
                                entrance = entranceChecked.date,
                                exit = exitChecked.date
                            )
                        )
                        entrance = null
                        exit = null
                    }
                }
            }
        }
        return dayRecords.toList()
    }

    suspend fun saveHours(latitude: Double, longitude: Double, entering: Boolean) {
        hoursRepository.saveHours(latitude, longitude, entering)
    }
}