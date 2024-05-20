package com.test.employeepresence.places.domain

const val UNKNOWN_CACHE_ID = -1L
data class WorkingPlace(val cacheId: Long = UNKNOWN_CACHE_ID, val latitude: Double, val longitude: Double, val address: String?)
