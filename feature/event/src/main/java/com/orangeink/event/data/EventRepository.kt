package com.orangeink.event.data

import com.orangeink.network.Resource
import com.orangeink.network.model.Event

interface EventRepository {
    suspend fun getEventDetails(eventId: Int): Resource<Event>
}