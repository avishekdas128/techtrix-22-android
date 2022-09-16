package com.orangeink.network

import com.orangeink.network.service.NotificationService
import com.orangeink.network.service.NotificationService.Companion.NOTIFICATION_ENDPOINT
import com.orangeink.network.service.TechTrixService
import com.orangeink.network.service.TechTrixService.Companion.ENDPOINT
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object APIModule {

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(logging: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor {
                val request = it.request()
                    .newBuilder()
                    .addHeader("Authorization", "Bearer " + BuildConfig.API_KEY)
                    .build()
                it.proceed(request)
            }
            .addInterceptor(logging)
            .connectTimeout(15, TimeUnit.SECONDS) // Connect timeout
            .readTimeout(15, TimeUnit.SECONDS) // Read timeout
            .build()
    }

    @Provides
    @Singleton
    @Named("TECHTRIX")
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ENDPOINT)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @Named("NOTIFICATION")
    fun provideNotificationRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(NOTIFICATION_ENDPOINT)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideTechTrixService(@Named("TECHTRIX") retrofit: Retrofit): TechTrixService {
        return retrofit.create(TechTrixService::class.java)
    }

    @Provides
    @Singleton
    fun provideNotificationService(@Named("NOTIFICATION") retrofit: Retrofit): NotificationService {
        return retrofit.create(NotificationService::class.java)
    }
}