package com.hasib.doctorappointment.screens.appointmentList

import com.hasib.doctorappointment.model.Appointment
import com.hasib.doctorappointment.utils.Resources

data class AppointmentListState(
    val appointments: Resources<List<Appointment>> = Resources.Loading()
)