package com.example.weathersooq.ui.viewModels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.*
import com.example.datascourcesmodule.database_room.WeatherEntity
import com.example.datascourcesmodule.realm_db.AstronomyRlm
import com.example.datascourcesmodule.realm_db.WeatherEntityRealm
import com.example.datascourcesmodule.repository.Repository
import com.example.datascourcesmodule.util.NetworkResult
import com.example.modelsmodule.data.Weather
import com.example.modelsmodule.data.WeatherModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.Realm
import io.realm.RealmList
import io.realm.kotlin.executeTransactionAwait
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


 private fun insertCityIntoRoom(weatherToEntity: Weather,
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

    private fun insertCityIntoRealm(weatherToEntity: Weather, cityName:String) =
        viewModelScope.launch(Dispatchers.IO) {
        val db = Realm.getDefaultInstance()
            val astronomy: RealmList<AstronomyRlm> = astronomyToRlms(weatherToEntity)

            db .executeTransactionAwait(Dispatchers.IO){

            val weatherInfo = WeatherEntityRealm().apply {
                avgtempC = weatherToEntity.avgtempC
                maxtempC = weatherToEntity.maxtempC
                mintempC= weatherToEntity.mintempC
                id = cityName
                astronomyRlm = astronomy
            }

            it.insert(weatherInfo)

        }




        }

    private fun astronomyToRlms(weatherToEntity: Weather): RealmList<AstronomyRlm> {
        val astronomy: RealmList<AstronomyRlm> = RealmList()
        for (element in weatherToEntity.astronomy) {
            astronomy.add(
                AstronomyRlm(
                    element.moon_illumination,
                    element.moon_phase,
                    element.moonrise,
                    element.moonset,
                    element.sunrise,
                    element.sunset
                )
            )
        }
        return astronomy
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

       //insertCityIntoRoom(weatherModel.data.weather[0], townName ) //switch to room
        insertCityIntoRealm(weatherModel.data.weather[0], townName )
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