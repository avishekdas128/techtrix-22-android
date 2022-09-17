package com.orangeink.category.di

import com.orangeink.category.data.CategoryRepository
import com.orangeink.category.data.CategoryRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class CategoryModule {

    @Binds
    abstract fun provideCategoryRepository(impl: CategoryRepositoryImpl): CategoryRepository
}