package com.orangeink.list.data

import com.orangeink.network.Resource
import com.orangeink.network.model.Event
import com.orangeink.network.model.Sponsor
import com.orangeink.network.model.Team

interface ListRepository {
    suspend fun getAllEvents(): Resource<List<Event>>

    suspend fun getAllSponsor(): Resource<List<Sponsor>>

    suspend fun getAllTeam(): Resource<List<Team>>
}