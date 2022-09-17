package com.orangeink.category

import com.orangeink.network.service.TechTrixService
import com.orangeink.network.BaseDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepository @Inject constructor(
    private val service: TechTrixService
) : BaseDataSource() {

    suspend fun eventsByCategory(category: String) = getResult {
        service.eventsByCategory(category)
    }
}