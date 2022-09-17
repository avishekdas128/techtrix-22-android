package com.orangeink.category.data

import com.orangeink.network.Resource
import com.orangeink.network.model.Event

interface CategoryRepository {
    suspend fun eventsByCategory(category: String): Resource<List<Event>>
}