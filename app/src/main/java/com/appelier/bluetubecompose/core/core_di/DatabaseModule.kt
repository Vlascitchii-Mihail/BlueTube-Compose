package com.appelier.bluetubecompose.core.core_di

import android.content.Context
import androidx.room.Room
import com.appelier.bluetubecompose.core.core_database.YouTubeDatabase
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
    @Named("OriginalDatabase")
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): YouTubeDatabase {
        return Room.databaseBuilder(appContext, YouTubeDatabase::class.java, YouTubeDatabase.ROOM_DATABASE)
            .build()
    }
}