package com.orangeink.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orangeink.network.Resource
import com.orangeink.network.model.Participant
import com.orangeink.network.model.SuccessResponse
import com.orangeink.network.model.UpdateParticipant
import com.orangeink.profile.data.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ProfileRepository
) : ViewModel() {

    private val _participant = MutableLiveData<Participant>()
    val participant: LiveData<Participant> = _participant

    private val _update = MutableLiveData<SuccessResponse>()
    val update: LiveData<SuccessResponse> = _update

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

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