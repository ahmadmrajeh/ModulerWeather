package com.example.weathersooq.ui.viewModels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.*
import com.example.modelsmodule.data.Weather
import com.example.modelsmodule.data.WeatherModel
import com.example.datascourcesmodule.database.WeatherEntity
import com.example.datascourcesmodule.repository.Repository
import com.example.datascourcesmodule.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository:  Repository,
    application: Application
) : AndroidViewModel(application) {

    /**  Room Database  */

    val readWeather: LiveData<List<WeatherEntity>> = repository.getWeatherData().asLiveData()


  private fun insertCity(weatherToEntity: Weather,
                         cityName:String) =
        viewModelScope.launch(Dispatchers.IO) {

            repository.insertWeatherCity(
              WeatherEntity(
                    weatherToEntity.avgtempC,
                    weatherToEntity.maxtempC, weatherToEntity.mintempC,
                    cityName
                )
            )


        }



    /**  Retrofit  */
   var weatherResponse: MutableLiveData<NetworkResult<WeatherModel>> = MutableLiveData()


    fun readTownWeather(queries: String) = viewModelScope.launch {
        getWeatherSafeCall(queries)
    }

    private suspend fun getWeatherSafeCall(queries: String) {
         weatherResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.searchWeatherByCity(queries)
                weatherResponse.value = handleWeatherResponse(response)

                val currentWeatherResponse =   weatherResponse.value!!.data
                 if (currentWeatherResponse != null) {
                  offlineCacheWeather(currentWeatherResponse,queries)
                }
            } catch (e: Exception) {

                weatherResponse.value =NetworkResult.Error("City not found.")
            }
        } else {

            weatherResponse.value = NetworkResult.Error("No Internet Connection.")
        }
    }


    private fun offlineCacheWeather(weatherModel: WeatherModel, townName: String) {

       insertCity(weatherModel.data.weather[0], townName )
    }


    private fun handleWeatherResponse(response: Response<WeatherModel>): NetworkResult<WeatherModel> {

        when {
            response.message().toString().contains("timeout") -> {
                return  NetworkResult.Error("Timeout")
            }
            response.code() == 402 -> {
                return  NetworkResult.Error("API Key Limited.")
            }
            response.body()!!.data.weather.isEmpty() -> {
                return  NetworkResult.Error("City not found.")
            }
            response.isSuccessful -> {

                return  NetworkResult.Success(response.body()!!)
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }



    private fun hasInternetConnection(): Boolean {

        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

}