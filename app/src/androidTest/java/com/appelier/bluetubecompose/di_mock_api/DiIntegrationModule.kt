package com.appelier.bluetubecompose.di_mock_api

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.appelier.bluetubecompose.core.core_database.YouTubeDatabase
import com.appelier.bluetubecompose.core.core_database.YouTubeVideoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DiIntegrationModule {

    @Singleton
    @Provides
    fun provideYouTubeVideoDatabase(): YouTubeDatabase {
        return Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(), YouTubeDatabase::class.java
        ).build()
    }

    @Singleton
    @Provides
    fun provideYouTubeVideoDao(database: YouTubeDatabase): YouTubeVideoDao {
        return database.youTubeVideoDao
    }
}
