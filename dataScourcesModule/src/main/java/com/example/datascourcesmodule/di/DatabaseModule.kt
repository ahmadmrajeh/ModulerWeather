package com.example.datascourcesmodule.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import com.example.datascourcesmodule.database_room.WeatherDatabase
import com.example.datascourcesmodule.util.WEATHER_DB
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, WeatherDatabase::class.java, WEATHER_DB).build()

    @Singleton
    @Provides
    fun provideDao(database: WeatherDatabase) = database.weatherDao()
}