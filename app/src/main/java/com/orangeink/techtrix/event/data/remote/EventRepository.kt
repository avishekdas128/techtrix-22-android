package com.orangeink.techtrix.event.data.remote

import com.orangeink.techtrix.data.network.TechTrixService
import com.orangeink.techtrix.util.BaseDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventRepository @Inject constructor(
    private val service: TechTrixService
) : BaseDataSource() {

    suspend fun getEventDetails(eventId: Int) = getResult {
        service.eventDetails(eventId)
    }

}