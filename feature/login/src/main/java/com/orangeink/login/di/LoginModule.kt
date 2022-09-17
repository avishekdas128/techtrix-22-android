package com.orangeink.login.di

import com.orangeink.login.data.LoginRepository
import com.orangeink.login.data.LoginRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class LoginModule {

    @Binds
    abstract fun provideLoginRepository(impl: LoginRepositoryImpl): LoginRepository
}