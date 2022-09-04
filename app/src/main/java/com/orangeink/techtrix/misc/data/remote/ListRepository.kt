package com.orangeink.techtrix.misc.data.remote

import com.orangeink.techtrix.data.network.TechTrixService
import com.orangeink.techtrix.util.BaseDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ListRepository @Inject constructor(
    private val service: TechTrixService
) : BaseDataSource() {

    suspend fun getAllEvents() = getResult {
        service.allEvents()
    }

    suspend fun getAllSponsor() = getResult {
        service.allSponsors()
    }

    suspend fun getAllTeam() = getResult {
        service.allTeam()
    }

}