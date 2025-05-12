package com.vlascitchii.data_local.di

import android.content.Context
import androidx.room.Room
import com.vlascitchii.data_local.database.YouTubeDatabase
import com.vlascitchii.data_local.database.YouTubeVideoDao
import com.vlascitchii.data_local.source.utils.DatabaseContentManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
//    @Named("OriginalDatabase")
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): YouTubeDatabase {
        return Room.databaseBuilder(appContext, YouTubeDatabase::class.java, YouTubeDatabase.ROOM_DATABASE)
            .build()
    }

    //TODO: add correct injection source
    @Singleton
//    @Named("OriginalDatabase")
    @Provides
    fun provideYoutubeVideoDao(database: YouTubeDatabase): YouTubeVideoDao = database.youTubeVideoDao

    @Singleton
    @Provides
    fun provideDatabaseContentManager(youTubeVideoDao: YouTubeVideoDao) =
        DatabaseContentManager(youTubeVideoDao)
}