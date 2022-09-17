package com.orangeink.registration.data

import com.orangeink.network.BaseDataSource
import com.orangeink.network.model.Registration
import com.orangeink.network.service.TechTrixService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegistrationRepositoryImpl @Inject constructor(
    private val service: TechTrixService
) : BaseDataSource(), RegistrationRepository {

    override suspend fun getRegistrations(email: String) = getResult {
        service.getRegistrations(email)
    }

    override suspend fun register(registration: Registration) = getResult {
        service.register(registration)
    }

}