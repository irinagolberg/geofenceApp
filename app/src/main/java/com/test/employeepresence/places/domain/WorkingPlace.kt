package com.test.employeepresence.places.domain

import com.test.employeepresence.places.data.PlacesLocalDataSourceImpl


data class WorkingPlace(val latitude: Double, val longitude: Double, val address: String?) {
    fun toPlaceIdKey(): String {
        return "$latitude$DELIMITER$longitude$DELIMITER${address ?: ""}"
    }

    companion object {
        private const val DELIMITER = "]:["
        fun parseWorkingPlace(placeIdKey: String): WorkingPlace? {
            val parts = placeIdKey.split(DELIMITER)

            return if (parts.size >= 2) {
                val latitude = parts[0].toDoubleOrNull()
                val longitude = parts[1].toDoubleOrNull()
                val address = if (parts.size >= 3) parts[2] else null
                if (latitude != null && longitude != null) {
                    WorkingPlace(latitude, longitude, address)
                } else null
            } else null

        }
    }
}
