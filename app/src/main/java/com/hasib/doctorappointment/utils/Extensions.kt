package com.hasib.doctorappointment.utils

import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun String.toLocalTime(): LocalTime {
    val formatter = DateTimeFormatter.ofPattern("h:mm a", Locale.US)
    return LocalTime.parse(this, formatter)
}

fun LocalTime.toFormattedString(): String {
    val formatter = DateTimeFormatter.ofPattern("h:mm a", Locale.US)
    return this.format(formatter)
}