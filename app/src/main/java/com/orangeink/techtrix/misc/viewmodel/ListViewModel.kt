package com.orangeink.techtrix.misc.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orangeink.techtrix.event.data.model.Event
import com.orangeink.techtrix.misc.data.model.Sponsor
import com.orangeink.techtrix.misc.data.model.Team
import com.orangeink.techtrix.misc.data.remote.ListRepository
import com.orangeink.techtrix.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val repository: ListRepository
) : ViewModel() {

    private val _events = MutableLiveData<List<Event>>()
    val events: LiveData<List<Event>> = _events

    private val _sponsors = MutableLiveData<List<Sponsor>>()
    val sponsors: LiveData<List<Sponsor>> = _sponsors

    private val _teams = MutableLiveData<List<Team>>()
    val teams: LiveData<List<Team>> = _teams

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun getAllEvents() {
        viewModelScope.launch {
            when (val response = repository.getAllEvents()) {
                is Resource.Empty -> {}
                is Resource.Error -> _error.postValue(response.exception)
                is Resource.Success -> {
                    response.data.let {
                        _events.postValue(it)
                    }
                }
            }
        }
    }

    fun getAllSponsors() {
        viewModelScope.launch {
            when (val response = repository.getAllSponsor()) {
                is Resource.Empty -> {}
                is Resource.Error -> _error.postValue(response.exception)
                is Resource.Success -> {
                    response.data.let {
                        _sponsors.postValue(it)
                    }
                }
            }
        }
    }

    fun getAllTeam() {
        viewModelScope.launch {
            when (val response = repository.getAllTeam()) {
                is Resource.Empty -> {}
                is Resource.Error -> _error.postValue(response.exception)
                is Resource.Success -> {
                    response.data.let {
                        _teams.postValue(it)
                    }
                }
            }
        }
    }
}