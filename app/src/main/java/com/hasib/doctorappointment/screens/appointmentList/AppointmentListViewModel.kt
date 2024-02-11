package com.hasib.doctorappointment.screens.appointmentList

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hasib.doctorappointment.data.repos.AppointmentRepository
import com.hasib.doctorappointment.model.SlotStatus
import com.hasib.doctorappointment.utils.Resources
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppointmentListViewModel @Inject constructor(
    private val repository: AppointmentRepository
) : ViewModel() {

    var uiState by mutableStateOf(AppointmentListState())

//    init {
//        val firestore = Firebase.firestore.collection("appointments")
//        AppData.appointments.forEach { data ->
//            firestore.add(data).addOnCompleteListener {
//                Timber.tag(it.result.id).d(data.toString())
//            }
//        }
//    }

    fun loadAppointments(
        search: String = "",
        slotStatus: String = "ALL"
    ) {
        uiState = uiState.copy(query = search)
        getAppointmentList(search, slotStatus)
    }

    private fun getAppointmentList(
        query: String,
        status: String
    ) = viewModelScope.launch {
        repository.getAppointments().map { resources ->
            resources.data?.filter {
                if (query.isEmpty()) true else {
                    it.doctorName.contains(query, ignoreCase = true)
                }
            }?.filter {
                if (status == "ALL") {
                    true
                } else {
                    it.status == SlotStatus.valueOf(status)
                }
            }?.sortedWith(compareBy(
                { it.doctorName },
                { it.time }
            )) ?: emptyList()
        }.collect {
            uiState = uiState.copy(appointments = Resources.Success(it))
        }
    }

    fun bookAppointment(appointmentId: String) {
        viewModelScope.launch {
            repository.createBooking(appointmentId) {
                uiState = uiState.copy(
                    bookingStatus = it
                )
            }
        }
    }

    fun resetState() {
        uiState = uiState.copy(bookingStatus = false)
    }
}
