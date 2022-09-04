package com.orangeink.techtrix.registrations.data.remote

import com.orangeink.techtrix.data.network.TechTrixService
import com.orangeink.techtrix.registrations.data.model.Registration
import com.orangeink.techtrix.util.BaseDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegistrationRepository @Inject constructor(
    private val service: TechTrixService
) : BaseDataSource() {

    suspend fun getRegistrations(email: String) = getResult {
        service.getRegistrations(email)
    }

    suspend fun register(registration: Registration) = getResult {
        service.register(registration)
    }

}