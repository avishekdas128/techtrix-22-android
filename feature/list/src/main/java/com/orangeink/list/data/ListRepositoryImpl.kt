package com.orangeink.list.data

import com.orangeink.network.BaseDataSource
import com.orangeink.network.service.TechTrixService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ListRepositoryImpl @Inject constructor(
    private val service: TechTrixService
) : BaseDataSource(), ListRepository {

    override suspend fun getAllEvents() = getResult {
        service.allEvents()
    }

    override suspend fun getAllSponsor() = getResult {
        service.allSponsors()
    }

    override suspend fun getAllTeam() = getResult {
        service.allTeam()
    }

}