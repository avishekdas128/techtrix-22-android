package com.orangeink.login.data

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.orangeink.network.Resource
import com.orangeink.network.model.Participant

interface LoginRepository {

    suspend fun signUpWithEmail(
        firebaseAuth: FirebaseAuth,
        email: String,
        password: String
    ): FirebaseUser?

    suspend fun logInWithEmail(
        firebaseAuth: FirebaseAuth,
        email: String,
        password: String
    ): FirebaseUser?

    suspend fun logInWithGoogle(firebaseAuth: FirebaseAuth, token: AuthCredential): FirebaseUser?

    suspend fun getProfile(email: String): Resource<Participant>
}