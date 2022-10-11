package com.example.datascourcesmodule.repository
 import com.example.modelsmodule.data.WeatherModel
 import com.example.datascourcesmodule.database_room.WeatherDAO
import com.example.datascourcesmodule.database_room.WeatherEntity
import com.example.datascourcesmodule.retrofit.ApiRequests
import dagger.hilt.android.scopes.ActivityRetainedScoped

import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

@ActivityRetainedScoped
class Repository @Inject constructor(
    private val apiRequests: ApiRequests,
    private val weatherDAO: WeatherDAO

    ) {

    //room
    fun getWeatherData(): Flow<List<WeatherEntity>>    {
        return weatherDAO.readWeather()
    }

suspend fun insertWeatherCity (weather: WeatherEntity){
    weatherDAO.insertCity(weather)
}

    //retrofit
    suspend fun searchWeatherByCity(query: String):Response<WeatherModel> {
        return apiRequests.getWeatherTown(query)
    }

}