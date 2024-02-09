package com.hasib.doctorappointment.data

import com.hasib.doctorappointment.model.Appointment
import com.hasib.doctorappointment.model.SlotStatus

object AppData {
    val appointmentList = listOf(
        Appointment("Doctor A", "9:00 AM", SlotStatus.FINISHED, 12, 4),
        Appointment("Doctor A", "12:00 PM", SlotStatus.NOT_AVAILABLE, 4, 0),
        Appointment("Doctor A", "4:00 PM", SlotStatus.FillED_UP, 10, 0),
        Appointment("Doctor B", "8:00 AM", SlotStatus.FINISHED, 8, 0),
        Appointment("Doctor B", "12:00 AM", SlotStatus.FINISHED, 6, 2),
        Appointment("Doctor B", "3:00 AM", SlotStatus.BOOKED, 4, 2),
        Appointment("Doctor B", "4:00 AM", SlotStatus.AVAILABLE, 7, 2),
        Appointment("Doctor B", "7:00 AM", SlotStatus.FillED_UP, 6, 0),
        Appointment("Doctor C", "9:00 AM", SlotStatus.FINISHED, 4, 2),
        Appointment("Doctor C", "10:00 AM", SlotStatus.FINISHED, 4, 3),
        Appointment("Doctor C", "12:00 AM", SlotStatus.FINISHED, 6, 4),
        Appointment("Doctor C", "4:00 AM", SlotStatus.AVAILABLE, 8, 2),
        Appointment("Doctor C", "7:00 AM", SlotStatus.AVAILABLE, 6, 1),
    )

    object Routes {
        const val APPOINTMENT_LIST_SCREEN = "appointment_list_screen"
    }
}