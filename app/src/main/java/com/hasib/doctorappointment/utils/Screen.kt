package com.hasib.doctorappointment.utils

sealed class Screen(val route: String) {
    data object AppointmentListScreen : Screen("appointment_list_screen")
    data object SignInScreen : Screen("sign_in_screen")
}