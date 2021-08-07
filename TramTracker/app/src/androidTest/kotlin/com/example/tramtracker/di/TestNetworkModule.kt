package com.example.tramtracker.di

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.mockwebserver.MockWebServer
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named

@Module
@InstallIn(ApplicationComponent::class)
object TestNetworkModule {
    const val MOCK_WEB_SERVER_PORT: Int = 8000

    @Provides
    fun provideMockWebServer(): MockWebServer {
        return MockWebServer()
    }

    @Provides
    @Named("test_retrofit")
    fun provideMockRetrofit(moshi: Moshi): Retrofit.Builder {
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
    }
}