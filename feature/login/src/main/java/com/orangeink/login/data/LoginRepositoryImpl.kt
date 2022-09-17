package com.orangeink.login.data

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.orangeink.network.BaseDataSource
import com.orangeink.network.service.TechTrixService
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginRepositoryImpl @Inject constructor(
    private val service: TechTrixService
) : BaseDataSource(), LoginRepository {

    override suspend fun signUpWithEmail(
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

    override suspend fun logInWithEmail(
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

    override suspend fun logInWithGoogle(
        firebaseAuth: FirebaseAuth,
        token: AuthCredential
    ): FirebaseUser? {
        return try {
            val data = firebaseAuth
                .signInWithCredential(token)
                .await()
            data.user
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getProfile(email: String) = getResult {
        service.getParticipant(email)
    }
}