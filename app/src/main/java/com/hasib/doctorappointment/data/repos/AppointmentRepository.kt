package com.hasib.doctorappointment.data.repos

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.hasib.doctorappointment.model.AppointmentItem
import com.hasib.doctorappointment.model.SlotStatus
import com.hasib.doctorappointment.utils.Resources
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@ActivityScoped
class AppointmentRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val user: FirebaseUser?
) {
    fun getAppointments(): Flow<Resources<List<AppointmentItem>>> = callbackFlow {
        val snapshotStateListener: ListenerRegistration?

        try {
            snapshotStateListener = firestore.collection("appointments")
                .addSnapshotListener { snapshot, _ ->
                    val response = if (snapshot != null) {
                        val appointments = snapshot.documents.map {
                            AppointmentItem.fromSnapshot(it)
                        }
                        appointments
                    } else {
                        emptyList()
                    }
                    trySend(response)
                }
        } catch (e: Exception) {
            error(e.cause ?: e)
        }

        awaitClose {
            snapshotStateListener.remove()
        }
    }.combine(
        callbackFlow {
            val snapshotStateListener: ListenerRegistration?

            try {
                snapshotStateListener = firestore.collection("bookings")
                    .whereEqualTo("userId", user?.uid)
                    .whereEqualTo("time", LocalDate.now().toString())
                    .addSnapshotListener { snapshot, _ ->
                        val response = if (snapshot != null) {
                            val bookingIds =
                                snapshot.documents.map { it.getString("appointmentId") }
                            bookingIds
                        } else {
                            emptyList()
                        }
                        trySend(response)
                    }
            } catch (e: Exception) {
                error(e.cause ?: e)
            }

            awaitClose {
                snapshotStateListener.remove()
            }
        }
    ) { appointments, bookings ->
        val updatedAppointments = appointments.map { appointment ->
            val (id, _, time, status, _, available) = appointment

            appointment.copy(
                status = when {
                    status == SlotStatus.NOT_AVAILABLE -> SlotStatus.NOT_AVAILABLE
                    time.isBefore(LocalTime.now()) -> SlotStatus.FINISHED
                    bookings.contains(id) -> SlotStatus.BOOKED
                    available == 0 -> SlotStatus.FillED_UP
                    available > 0 -> SlotStatus.AVAILABLE
                    else -> SlotStatus.NOT_AVAILABLE
                }
            )
        }
        Resources.Success(updatedAppointments)
    }

    fun createBooking(
        appointmentId: String,
        onComplete: (Boolean) -> Unit
    ) {
        firestore.runTransaction {
            user?.uid?.let {
                firestore.collection("bookings").add(
                    mapOf(
                        "userId" to it,
                        "appointmentId" to appointmentId,
                        "time" to LocalDate.now().toString()
                    )
                )
            }

            firestore.collection("appointments")
                .document(appointmentId).update("available", FieldValue.increment(-1))
        }.addOnCompleteListener {
            onComplete(it.isSuccessful)
        }
    }
}