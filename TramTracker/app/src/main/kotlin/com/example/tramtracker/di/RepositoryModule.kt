package com.example.tramtracker.di

import android.content.Context
import com.example.tramtracker.network.api.TrackerApi
import com.example.tramtracker.repository.TrackerRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideTrackerRepository(
        @ApplicationContext context: Context,
        trackerApi: TrackerApi,
    ): TrackerRepository {
        return TrackerRepository(trackerApi)
    }
}