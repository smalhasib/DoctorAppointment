package com.hasib.doctorappointment.screens.appointmentList

import com.hasib.doctorappointment.model.AppointmentItem
import com.hasib.doctorappointment.utils.Resources

data class AppointmentListState(
    val appointments: Resources<List<AppointmentItem>> = Resources.Loading(),
    val query: String = "",
    val bookingStatus: Boolean = false
)