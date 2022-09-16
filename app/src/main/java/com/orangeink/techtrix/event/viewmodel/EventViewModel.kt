package com.orangeink.techtrix.event.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orangeink.network.model.Event
import com.orangeink.techtrix.event.data.remote.EventRepository
import com.orangeink.network.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val repository: EventRepository
) : ViewModel() {

    private val _eventDetails = MutableLiveData<Event>()
    val eventDetails: LiveData<Event> = _eventDetails

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun getEventDetails(eventId: Int) {
        viewModelScope.launch {
            when (val response = repository.getEventDetails(eventId)) {
                is Resource.Empty -> {}
                is Resource.Error -> _error.postValue(response.exception)
                is Resource.Success -> {
                    response.data.let {
                        _eventDetails.postValue(it)
                    }
                }
            }
        }
    }
}