package com.test.employeepresence.hours.domain

import com.test.employeepresence.places.domain.WorkingPlace
import java.util.Date

data class HoursRecord (val date: Date, val type: HoursRecordType, val place: WorkingPlace)
enum class HoursRecordType {ENTER, EXIT}