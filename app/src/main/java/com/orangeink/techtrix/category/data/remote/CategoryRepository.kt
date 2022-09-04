package com.orangeink.techtrix.category.data.remote

import com.orangeink.techtrix.data.network.TechTrixService
import com.orangeink.techtrix.util.BaseDataSource
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