package com.orangeink.profile.data

import com.orangeink.network.Resource
import com.orangeink.network.model.Participant
import com.orangeink.network.model.SuccessResponse
import com.orangeink.network.model.UpdateParticipant

interface ProfileRepository {

    suspend fun createProfile(participant: Participant): Resource<Participant>

    suspend fun updateProfile(
        participant: UpdateParticipant,
        email: String
    ): Resource<SuccessResponse>
}