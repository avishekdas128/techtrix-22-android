package com.orangeink.techtrix.search.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orangeink.techtrix.event.data.model.Event
import com.orangeink.techtrix.search.data.remote.SearchRepository
import com.orangeink.techtrix.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: SearchRepository
) : ViewModel() {

    private val _search = MutableLiveData<List<Event>>()
    val search: LiveData<List<Event>> = _search

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun search(query: String) {
        viewModelScope.launch {
            when (val response = repository.search(query)) {
                is Resource.Empty -> _search.postValue(arrayListOf())
                is Resource.Error -> _error.postValue(response.exception)
                is Resource.Success -> {
                    response.data.searchResult?.let {
                        _search.postValue(it)
                    }
                }
            }
        }
    }
}