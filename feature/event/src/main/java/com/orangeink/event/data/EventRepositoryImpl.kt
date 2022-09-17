package com.orangeink.event.data

import com.orangeink.network.BaseDataSource
import com.orangeink.network.service.TechTrixService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventRepositoryImpl @Inject constructor(
    private val service: TechTrixService
) : BaseDataSource(), EventRepository {

    override suspend fun getEventDetails(eventId: Int) = getResult {
        service.eventDetails(eventId)
    }

}