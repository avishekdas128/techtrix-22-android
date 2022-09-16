package com.orangeink.techtrix.login.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.orangeink.network.model.SuccessResponse
import com.orangeink.network.model.Participant
import com.orangeink.network.model.UpdateParticipant
import com.orangeink.techtrix.login.data.remote.LoginRepository
import com.orangeink.network.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: LoginRepository
) : ViewModel() {

    private val _user = MutableLiveData<FirebaseUser>()
    val user: LiveData<FirebaseUser> = _user

    private val _participant = MutableLiveData<Participant>()
    val participant: LiveData<Participant> = _participant

    private val _generalFees = MutableLiveData<Participant>()
    val generalFees: LiveData<Participant> = _generalFees

    private val _update = MutableLiveData<SuccessResponse>()
    val update: LiveData<SuccessResponse> = _update

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun loginWithEmail(auth: FirebaseAuth, email: String, password: String) {
        viewModelScope.launch {
            repository.logInWithEmail(auth, email, password)?.let {
                when (val response = repository.getProfile(it.email!!)) {
                    is Resource.Error -> _user.postValue(it)
                    is Resource.Success -> {
                        response.data.let { participant ->
                            _participant.postValue(participant)
                        }
                    }
                    else -> {}
                }
            } ?: repository.signUpWithEmail(auth, email, password)?.let {
                _user.postValue(it)
            } ?: _error.postValue("Something went wrong!")
        }
    }

    fun loginWithGoogle(auth: FirebaseAuth, credential: AuthCredential) {
        viewModelScope.launch {
            repository.logInWithGoogle(auth, credential)?.let {
                when (val response = repository.getProfile(it.email!!)) {
                    is Resource.Error -> _user.postValue(it)
                    is Resource.Success -> {
                        response.data.let { participant ->
                            _participant.postValue(participant)
                        }
                    }
                    else -> {}
                }
            } ?: _error.postValue("Something went wrong!")
        }
    }

    fun createParticipant(participant: Participant) {
        viewModelScope.launch {
            when (val response = repository.createProfile(participant)) {
                is Resource.Error -> _error.postValue(response.exception)
                is Resource.Success -> {
                    response.data.let {
                        _participant.postValue(it)
                    }
                }
                else -> {}
            }
        }
    }

    fun getProfile(email: String) {
        viewModelScope.launch {
            when (val response = repository.getProfile(email)) {
                is Resource.Error -> _error.postValue(response.exception)
                is Resource.Success -> {
                    response.data.let {
                        _generalFees.postValue(it)
                    }
                }
                else -> {}
            }
        }
    }

    fun updateProfile(participant: UpdateParticipant, email: String) {
        viewModelScope.launch {
            when (val response = repository.updateProfile(participant, email)) {
                is Resource.Error -> _error.postValue(response.exception)
                is Resource.Success -> {
                    response.data.let {
                        _update.postValue(it)
                    }
                }
                else -> {}
            }
        }
    }
}