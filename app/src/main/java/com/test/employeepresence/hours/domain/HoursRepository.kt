package com.test.employeepresence.hours.domain

interface HoursRepository {
    suspend fun saveHours(hours: HoursRecord)
    suspend fun getHours(): List<HoursRecord>
}