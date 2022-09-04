package com.orangeink.techtrix.home.data.remote

import com.orangeink.techtrix.data.network.TechTrixService
import com.orangeink.techtrix.util.BaseDataSource
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