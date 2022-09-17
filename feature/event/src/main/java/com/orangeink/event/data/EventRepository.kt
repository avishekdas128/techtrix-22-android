package com.orangeink.event.data

import com.orangeink.network.service.TechTrixService
import com.orangeink.network.BaseDataSource
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