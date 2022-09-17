package com.orangeink.list.di

import com.orangeink.list.data.ListRepository
import com.orangeink.list.data.ListRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class ListModule {

    @Binds
    abstract fun provideListRepository(impl: ListRepositoryImpl): ListRepository
}