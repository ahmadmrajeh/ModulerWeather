package com.example.datascourcesmodule.database_room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCity(weatherEntity: WeatherEntity)



    @Query("SELECT * FROM  weather_table")
    fun readWeather(): Flow<List<WeatherEntity>>




}