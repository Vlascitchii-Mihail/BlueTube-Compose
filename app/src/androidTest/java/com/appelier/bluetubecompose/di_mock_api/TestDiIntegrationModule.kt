package com.appelier.bluetubecompose.di_mock_api

import android.content.Context
import androidx.room.Room
import com.vlascitchii.data_local.database.YouTubeDatabase
import com.vlascitchii.data_local.database.YouTubeVideoDao
import com.vlascitchii.data_local.di.DatabaseModule
import com.vlascitchii.data_local.source.utils.DatabaseContentManager
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatabaseModule::class]
)
object TestDiIntegrationModule {

    @Singleton
    @Provides
    fun provideInMemoryDatabase(@ApplicationContext appContext: Context): YouTubeDatabase {
        return Room.inMemoryDatabaseBuilder(appContext, YouTubeDatabase::class.java).build()
    }

    @Singleton
    @Provides
    fun provideFakeYouTubeVideoDao(database: YouTubeDatabase): YouTubeVideoDao {
        return database.youTubeVideoDao
    }

    @Singleton
    @Provides
    fun provideFakeDatabaseContentManager(youTubeVideoDao: YouTubeVideoDao) =
        DatabaseContentManager(youTubeVideoDao)
}
