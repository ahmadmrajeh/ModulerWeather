package com.example.datascourcesmodule.retrofit

import com.example.modelsmodule.data.WeatherModel
import com.example.datascourcesmodule.util.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiRequests {


    @GET("premium/v1/weather.ashx")
    suspend fun getWeatherTown(
        @Query("q") city: String,
        @Query("num_of_days") NumOfDays: Int=1,
        @Query("date") date: String="today",
        @Query("format") format: String="json",
        @Query("key") apiKey: String = API_KEY,

        ): Response<WeatherModel>

}
