package com.orangeink.home.data

import com.orangeink.network.BaseDataSource
import com.orangeink.network.service.TechTrixService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepository @Inject constructor(
    private val service: TechTrixService
) : BaseDataSource() {

    suspend fun loadHomePage(versionCode: Int) = getResult {
        service.loadHome(versionCode)
    }

}