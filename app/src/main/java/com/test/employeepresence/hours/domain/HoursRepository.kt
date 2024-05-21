package com.test.employeepresence.hours.domain

interface HoursRepository {
    suspend fun saveHours(latitiude: Double, longitude: Double, entering: Boolean)
    suspend fun getHours(): List<HoursRecord>
}