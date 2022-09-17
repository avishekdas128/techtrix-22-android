package com.orangeink.search.di

import com.orangeink.search.data.SearchRepository
import com.orangeink.search.data.SearchRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class SearchModule {

    @Binds
    abstract fun provideSearchRepository(impl: SearchRepositoryImpl): SearchRepository
}