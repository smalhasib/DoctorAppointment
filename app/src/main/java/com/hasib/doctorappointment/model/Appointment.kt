package com.hasib.doctorappointment.model

import androidx.annotation.Keep

data class Appointment(
    val doctorName: String,
    val time: String,
    val isDoctorAvailable: Boolean,
    val total: Int,
    val available: Int
) {
    @Keep
    constructor() : this("", "", true, 0, 0)
}
