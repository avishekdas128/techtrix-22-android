package com.orangeink.techtrix.registrations.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orangeink.network.model.SuccessResponse
import com.orangeink.network.model.Registration
import com.orangeink.techtrix.registrations.data.remote.RegistrationRepository
import com.orangeink.network.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val repository: RegistrationRepository
) : ViewModel() {

    private val _registrations = MutableLiveData<List<Registration>>()
    val registrations: LiveData<List<Registration>> = _registrations

    private val _createdRegistration = MutableLiveData<SuccessResponse>()
    val createdRegistration: LiveData<SuccessResponse> = _createdRegistration

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun getRegistrations(email: String) {
        viewModelScope.launch {
            when (val response = repository.getRegistrations(email)) {
                is Resource.Empty -> {}
                is Resource.Error -> _error.postValue(response.exception)
                is Resource.Success -> {
                    response.data.let {
                        _registrations.postValue(it)
                    }
                }
            }
        }
    }

    fun register(registration: Registration) {
        viewModelScope.launch {
            when (val response = repository.register(registration)) {
                is Resource.Empty -> {}
                is Resource.Error -> _error.postValue(response.exception)
                is Resource.Success -> {
                    response.data.let {
                        _createdRegistration.postValue(it)
                    }
                }
            }
        }
    }
}