package com.orangeink.search.data

import com.orangeink.network.service.TechTrixService
import com.orangeink.network.BaseDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepository @Inject constructor(
    private val service: TechTrixService
) : BaseDataSource() {

    suspend fun search(query: String) = getResult {
        service.search(query)
    }

}