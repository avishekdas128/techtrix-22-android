package com.orangeink.profile.di

import com.orangeink.profile.data.ProfileRepository
import com.orangeink.profile.data.ProfileRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class ProfileModule {

    @Binds
    abstract fun provideProfileRepository(impl: ProfileRepositoryImpl): ProfileRepository
}