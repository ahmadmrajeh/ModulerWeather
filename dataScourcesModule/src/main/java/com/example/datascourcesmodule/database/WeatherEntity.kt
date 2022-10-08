package com.example.datascourcesmodule.database



import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.datascourcesmodule.util.WEATHER_TABLE
import org.jetbrains.annotations.NotNull

@Entity(tableName = WEATHER_TABLE)
data class WeatherEntity(

    val avgtempC: String?,

    val maxtempC: String?,

    val mintempC: String?,
    @PrimaryKey
     var id: String
)