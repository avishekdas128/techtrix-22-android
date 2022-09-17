package com.orangeink.event.di

import com.orangeink.event.data.EventRepository
import com.orangeink.event.data.EventRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class EventModule {

    @Binds
    abstract fun provideEventRepository(impl: EventRepositoryImpl): EventRepository
}