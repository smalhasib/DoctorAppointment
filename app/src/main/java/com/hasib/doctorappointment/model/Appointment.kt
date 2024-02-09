package com.hasib.doctorappointment.model

data class Appointment(
    val doctorName: String,
    val time: String,
    val status: SlotStatus,
    val total: Int,
    val available: Int
)

enum class SlotStatus {
    AVAILABLE,
    BOOKED,
    FillED_UP,
    FINISHED,
    NOT_AVAILABLE
}
