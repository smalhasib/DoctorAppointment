package com.hasib.doctorappointment.data

import com.hasib.doctorappointment.model.Appointment

object AppData {
    // Seed data for the app
    val appointments = listOf(
        Appointment("Doctor A", "9:00 AM", true, 12, 4),
        Appointment("Doctor A", "12:00 PM", false, 4, 0),
        Appointment("Doctor A", "4:00 PM", true, 10, 0),
        Appointment("Doctor B", "8:00 AM", true, 8, 0),
        Appointment("Doctor B", "12:00 AM", true, 6, 2),
        Appointment("Doctor B", "3:00 AM", true, 4, 2),
        Appointment("Doctor B", "4:00 AM", true, 7, 2),
        Appointment("Doctor B", "7:00 AM", true, 6, 0),
        Appointment("Doctor C", "9:00 AM", true, 4, 2),
        Appointment("Doctor C", "10:00 AM", true, 4, 3),
        Appointment("Doctor C", "12:00 AM", true, 6, 4),
        Appointment("Doctor C", "4:00 AM", true, 8, 2),
        Appointment("Doctor C", "7:00 AM", true, 6, 1),
        Appointment("Doctor C", "9:00 PM", true, 6, 1),
        Appointment("Doctor D", "11:00 PM", true, 6, 3),
        Appointment("Doctor D", "7:00 PM", true, 6, 0),
        Appointment("Doctor E", "10:00 PM", true, 6, 1),
        Appointment("Doctor E", "11:00 PM", true, 6, 1),
    )
}