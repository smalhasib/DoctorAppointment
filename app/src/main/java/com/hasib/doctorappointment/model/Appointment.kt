package com.hasib.doctorappointment.model

import androidx.annotation.Keep

data class Appointment(
    val doctorName: String,
    val time: String,
    val status: SlotStatus,
    val total: Int,
    val available: Int
) {
    @Keep
    constructor() : this("", "", SlotStatus.AVAILABLE, 0, 0)
}

enum class SlotStatus {
    AVAILABLE,
    BOOKED,
    FillED_UP,
    FINISHED,
    NOT_AVAILABLE
}
