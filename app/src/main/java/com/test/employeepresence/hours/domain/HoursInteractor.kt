package com.test.employeepresence.hours.domain

import android.util.Log
import com.test.employeepresence.places.domain.PlacesRepository
import com.test.employeepresence.places.domain.WorkingPlace
import com.test.employeepresence.utils.APP_LOGTAG
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

class HoursInteractor @Inject constructor(private val placesRepository: PlacesRepository, private val hoursRepository: HoursRepository) {
    suspend fun getHours(): List<DayRecord> {
        val dayRecords = mutableListOf<DayRecord>()
        val hours = hoursRepository.getHours()
        val entrances = hours.filter { it.type == HoursRecordType.ENTER }
        val exits = hours.filter { it.type == HoursRecordType.EXIT }.associate { getDayKey(it.date) to it.date }
        Log.d(APP_LOGTAG, "getHours hours: $hours")
        Log.d(APP_LOGTAG, "getHours entrance: $entrances")
        Log.d(APP_LOGTAG, "getHours exits: $exits")
        entrances.forEach { entrance ->
            val key = getDayKey(entrance.date)
            exits[key]?.let { exit ->
                dayRecords.add(DayRecord(entrance = entrance.date, exit = exit))
            }
        }
        return dayRecords.toList()
    }
    private fun getDayKey(date: Date): String {
        /*val calendar = Calendar.getInstance()
        calendar.timeInMillis = date.time
        val key = "${calendar.get(Calendar.YEAR)}:${calendar.get(Calendar.DAY_OF_MONTH)}:${calendar.get(Calendar.MONTH)}"
        Log.d(APP_LOGTAG, "getDayKey: $key")*/
        return SimpleDateFormat("dd.MM.yyyy").format(date)
    }
    suspend fun saveHours(latitude: Double?, longitude: Double?, entering: Boolean) {
        val record = HoursRecord(
            date = Calendar.getInstance().time, type =
            when (entering) {
                true -> HoursRecordType.ENTER
                false -> HoursRecordType.EXIT
            }
        )
        hoursRepository.saveHours(record)
    }
}