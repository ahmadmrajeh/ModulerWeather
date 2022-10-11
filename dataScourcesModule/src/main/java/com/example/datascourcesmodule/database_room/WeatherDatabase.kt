package com.example.datascourcesmodule.database_room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [WeatherEntity::class ] ,
    version = 1,
    exportSchema = false
)

@TypeConverters(TypeConverter::class)
abstract class WeatherDatabase : RoomDatabase() {

    abstract fun weatherDao(): WeatherDAO
}
