package com.orangeink.techtrix.login.data.remote

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.orangeink.techtrix.data.network.TechTrixService
import com.orangeink.techtrix.login.data.model.Participant
import com.orangeink.techtrix.login.data.model.UpdateParticipant
import com.orangeink.techtrix.util.BaseDataSource
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginRepository @Inject constructor(
    private val service: TechTrixService
) : BaseDataSource() {

    suspend fun signUpWithEmail(
        firebaseAuth: FirebaseAuth,
        email: String,
        password: String
    ): FirebaseUser? {
        return try {
            val data = firebaseAuth
                .createUserWithEmailAndPassword(email, password)
                .await()
            data.user
        } catch (e: Exception) {
            Timber.e(e)
            null
        }
    }

    suspend fun logInWithEmail(
        firebaseAuth: FirebaseAuth,
        email: String,
        password: String
    ): FirebaseUser? {
        return try {
            val data = firebaseAuth
                .signInWithEmailAndPassword(email, password)
                .await()
            data.user
        } catch (e: Exception) {
            Timber.e(e)
            null
        }
    }

    suspend fun logInWithGoogle(firebaseAuth: FirebaseAuth, token: AuthCredential): FirebaseUser? {
        return try {
            val data = firebaseAuth
                .signInWithCredential(token)
                .await()
            data.user
        } catch (e: Exception) {
            null
        }
    }

    suspend fun createProfile(participant: Participant) = getResult {
        service.createParticipant(participant)
    }

    suspend fun getProfile(email: String) = getResult {
        service.getParticipant(email)
    }

    suspend fun updateProfile(participant: UpdateParticipant, email: String) = getResult {
        service.updateParticipant(email, participant)
    }
}