package com.orangeink.techtrix.search.data.remote

import com.orangeink.techtrix.data.network.TechTrixService
import com.orangeink.techtrix.util.BaseDataSource
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