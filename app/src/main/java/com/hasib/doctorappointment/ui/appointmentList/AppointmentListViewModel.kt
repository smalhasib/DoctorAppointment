package com.hasib.doctorappointment.ui.appointmentList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hasib.doctorappointment.data.AppData
import com.hasib.doctorappointment.model.Appointment
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppointmentListViewModel @Inject constructor(

) : ViewModel() {
    private val _searchString = MutableLiveData("")
    val searchString: LiveData<String> = _searchString

    private val _appointmentList = MutableLiveData<List<Appointment>>(AppData.appointmentList)
    val appointmentList: LiveData<List<Appointment>> = _appointmentList
}