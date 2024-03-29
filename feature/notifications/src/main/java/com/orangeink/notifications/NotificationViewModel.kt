package com.orangeink.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orangeink.network.model.Notification
import com.orangeink.network.Resource
import com.orangeink.notifications.data.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val repository: NotificationRepository
) : ViewModel() {

    private val _notifications = MutableLiveData<List<Notification>>()
    val notifications: LiveData<List<Notification>> = _notifications

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun getNotifications() {
        viewModelScope.launch {
            when (val response = repository.getNotifications()) {
                is Resource.Empty -> _notifications.postValue(arrayListOf())
                is Resource.Error -> _error.postValue(response.exception)
                is Resource.Success -> {
                    response.data.let {
                        _notifications.postValue(it)
                    }
                }
            }
        }
    }
}