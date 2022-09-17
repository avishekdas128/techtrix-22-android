package com.orangeink.category.data

import com.orangeink.network.BaseDataSource
import com.orangeink.network.service.TechTrixService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepositoryImpl @Inject constructor(
    private val service: TechTrixService
) : BaseDataSource(), CategoryRepository {

    override suspend fun eventsByCategory(category: String) = getResult {
        service.eventsByCategory(category)
    }
}