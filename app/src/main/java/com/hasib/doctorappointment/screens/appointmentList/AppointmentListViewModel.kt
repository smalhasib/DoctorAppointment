package com.hasib.doctorappointment.screens.appointmentList

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hasib.doctorappointment.data.AppData
import com.hasib.doctorappointment.data.repos.AppointmentRepository
import com.hasib.doctorappointment.model.Appointment
import com.hasib.doctorappointment.utils.Resources
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppointmentListViewModel @Inject constructor(
    private val repository: AppointmentRepository
) : ViewModel() {
    private val _searchString = MutableLiveData("")
    val searchString: LiveData<String> = _searchString

    var appointmentListState by mutableStateOf(AppointmentListState())

//    init {
//        val firestore = Firebase.firestore.collection("appointments")
//        AppData.appointmentList.forEach {
//            firestore.add(it)
//        }
//    }

    fun loadAppointments() {
        getAppointmentList()
    }

    private fun getAppointmentList() = viewModelScope.launch {
        repository.getAppointments().collect {
            appointmentListState = appointmentListState.copy(appointments = it)
        }
    }
}
