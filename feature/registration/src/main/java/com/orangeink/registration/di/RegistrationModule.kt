package com.orangeink.registration.di

import com.orangeink.registration.data.RegistrationRepository
import com.orangeink.registration.data.RegistrationRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RegistrationModule {

    @Binds
    abstract fun provideRegistrationRepository(impl: RegistrationRepositoryImpl): RegistrationRepository
}