package com.orangeink.techtrix.registrations.data.remote

import com.orangeink.network.service.TechTrixService
import com.orangeink.network.model.Registration
import com.orangeink.network.BaseDataSource
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