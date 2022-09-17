package com.orangeink.home.di

import com.orangeink.home.data.HomeRepository
import com.orangeink.home.data.HomeRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class HomeModule {

    @Binds
    abstract fun provideHomeRepository(impl: HomeRepositoryImpl): HomeRepository
}