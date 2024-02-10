package com.hasib.doctorappointment.data.repos

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.hasib.doctorappointment.model.Appointment
import com.hasib.doctorappointment.utils.Resources
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

@ActivityScoped
class AppointmentRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    fun getAppointments(): Flow<Resources<List<Appointment>>> = callbackFlow {
        var snapshotStateListener: ListenerRegistration? = null

        try {
            snapshotStateListener = firestore.collection("appointments")
                .addSnapshotListener { snapshot, e ->
                    val response = if (snapshot != null) {
                        val appointments = snapshot.toObjects(Appointment::class.java)
                        Resources.Success(data = appointments)
                    } else {
                        Resources.Error(throwable = e?.cause)
                    }
                    trySend(response)
                }
        } catch (e: Exception) {
            trySend(Resources.Error(e.cause))
            e.printStackTrace()
        }

        awaitClose {
            snapshotStateListener?.remove()
        }
    }
}