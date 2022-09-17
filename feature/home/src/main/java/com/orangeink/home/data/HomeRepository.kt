package com.orangeink.home.data

import com.orangeink.network.Resource
import com.orangeink.network.model.HomeResponse

interface HomeRepository {
    suspend fun loadHomePage(versionCode: Int): Resource<HomeResponse>
}