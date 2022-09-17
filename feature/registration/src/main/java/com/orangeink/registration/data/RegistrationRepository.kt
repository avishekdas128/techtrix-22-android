package com.orangeink.registration.data

import com.orangeink.network.Resource
import com.orangeink.network.model.Registration
import com.orangeink.network.model.SuccessResponse

interface RegistrationRepository {
    suspend fun getRegistrations(email: String): Resource<List<Registration>>

    suspend fun register(registration: Registration): Resource<SuccessResponse>
}