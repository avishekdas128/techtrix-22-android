package com.orangeink.techtrix.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orangeink.techtrix.home.data.model.HomeResponse
import com.orangeink.techtrix.home.data.remote.HomeRepository
import com.orangeink.techtrix.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HomeRepository
) : ViewModel() {

    private val _homeResponse = MutableLiveData<HomeResponse>()
    val homeResponse: LiveData<HomeResponse> = _homeResponse

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun loadHome(versionCode: Int) {
        viewModelScope.launch {
            when (val response = repository.loadHomePage(versionCode)) {
                is Resource.Error -> _error.postValue(response.exception)
                is Resource.Success -> {
                    response.data.let {
                        _homeResponse.postValue(it)
                    }
                }
                else -> {}
            }
        }
    }
}