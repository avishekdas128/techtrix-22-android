package com.orangeink.search.data

import com.orangeink.network.Resource
import com.orangeink.network.model.SearchResponse

interface SearchRepository {
    suspend fun search(query: String): Resource<SearchResponse>
}