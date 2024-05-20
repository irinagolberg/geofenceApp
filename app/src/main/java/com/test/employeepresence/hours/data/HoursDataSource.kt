package com.test.employeepresence.hours.data

import com.test.employeepresence.hours.domain.HoursRecord

interface HoursDataSource {
    suspend fun saveHours(record: HoursRecord): Long
    suspend fun getHours(placeId: Long?): List<HoursRecord>

}