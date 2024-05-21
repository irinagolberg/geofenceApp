package com.test.employeepresence.hours.data

import com.test.employeepresence.hours.domain.HoursRecord
import com.test.employeepresence.places.domain.WorkingPlace

interface HoursDataSource {
    suspend fun saveHours(record: HoursRecord): Long
    suspend fun getHours(place: WorkingPlace): List<HoursRecord>
}