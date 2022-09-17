package com.orangeink.profile.data

import com.orangeink.network.BaseDataSource
import com.orangeink.network.model.Participant
import com.orangeink.network.model.UpdateParticipant
import com.orangeink.network.service.TechTrixService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepository @Inject constructor(
    private val service: TechTrixService
) : BaseDataSource() {

    suspend fun createProfile(participant: Participant) = getResult {
        service.createParticipant(participant)
    }

    suspend fun updateProfile(participant: UpdateParticipant, email: String) = getResult {
        service.updateParticipant(email, participant)
    }
}