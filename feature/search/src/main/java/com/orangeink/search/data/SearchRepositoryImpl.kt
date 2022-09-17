package com.orangeink.search.data

import com.orangeink.network.BaseDataSource
import com.orangeink.network.service.TechTrixService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepositoryImpl @Inject constructor(
    private val service: TechTrixService
) : BaseDataSource(), SearchRepository {

    override suspend fun search(query: String) = getResult {
        service.search(query)
    }

}