package com.orangeink.notifications.di

import com.orangeink.notifications.data.NotificationRepository
import com.orangeink.notifications.data.NotificationRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class NotificationModule {

    @Binds
    abstract fun provideNotificationRepository(impl: NotificationRepositoryImpl): NotificationRepository
}