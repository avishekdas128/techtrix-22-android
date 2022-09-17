package com.orangeink.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orangeink.category.data.CategoryRepository
import com.orangeink.network.Resource
import com.orangeink.network.model.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val repository: CategoryRepository
) : ViewModel() {

    private val _events = MutableLiveData<List<Event>>()
    val events: LiveData<List<Event>> = _events

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun eventsByCategory(category: String) {
        viewModelScope.launch {
            when (val response = repository.eventsByCategory(category)) {
                is Resource.Empty -> _events.postValue(arrayListOf())
                is Resource.Error -> _error.postValue(response.exception)
                is Resource.Success -> {
                    response.data.let {
                        _events.postValue(it)
                    }
                }
            }
        }
    }
}