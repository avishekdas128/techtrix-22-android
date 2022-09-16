package com.orangeink.techtrix.di

import com.orangeink.techtrix.navigator.AppNavigatorImpl
import com.orangeink.common.navigator.IAppNavigator
import dagger.Binds
import dagger.Module
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Reusable
    abstract fun provideAppNavigator(appNavigatorImpl: AppNavigatorImpl): IAppNavigator
}