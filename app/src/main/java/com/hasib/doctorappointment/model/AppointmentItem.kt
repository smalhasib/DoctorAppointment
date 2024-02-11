package com.hasib.doctorappointment.model

import androidx.compose.ui.graphics.Color
import com.google.firebase.firestore.DocumentSnapshot
import com.hasib.doctorappointment.utils.toLocalTime
import java.time.LocalTime

data class AppointmentItem(
    val id: String,
    val doctorName: String,
    val time: LocalTime,
    val status: SlotStatus,
    val total: Int,
    val available: Int
) {
    companion object {
        fun fromSnapshot(data: DocumentSnapshot): AppointmentItem {
            return AppointmentItem(
                id = data.id,
                doctorName = data["doctorName"] as String,
                time = (data["time"] as String).toLocalTime(),
                status = if (data["doctorAvailable"] as Boolean) SlotStatus.AVAILABLE else SlotStatus.NOT_AVAILABLE,
                total = (data["total"] as Long).toInt(),
                available = (data["available"] as Long).toInt()
            )
        }
    }
}

enum class SlotStatus {
    AVAILABLE,
    BOOKED,
    FillED_UP,
    FINISHED,
    NOT_AVAILABLE
}

fun SlotStatus.chipColor(): Color {
    return when (this) {
        SlotStatus.AVAILABLE -> Color(0xFFD4EDBD)
        SlotStatus.BOOKED -> Color(0xFF5B3286)
        SlotStatus.FillED_UP -> Color(0xFFB10202)
        SlotStatus.FINISHED -> Color(0xFFFBB1B1)
        SlotStatus.NOT_AVAILABLE -> Color(0xFFE6E6E6)
    }
}
