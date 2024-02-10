package com.hasib.doctorappointment.di

import com.google.firebase.firestore.FirebaseFirestore
import com.hasib.doctorappointment.data.repos.AppointmentRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Singleton
    @Provides
    fun provideAppointmentRepository(firestore: FirebaseFirestore) = AppointmentRepository(firestore)
}