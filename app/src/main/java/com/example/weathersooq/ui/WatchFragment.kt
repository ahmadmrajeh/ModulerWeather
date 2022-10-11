package com.example.weathersooq.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.datascourcesmodule.realm_db.WeatherEntityRealm
import com.example.weathersooq.R
import com.example.weathersooq.databinding.FragmentWatchBinding
import com.example.weathersooq.ui.viewModels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.realm.Realm
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WatchFragment : Fragment() {
    private lateinit var binding: FragmentWatchBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWatchBinding.inflate(inflater)




    //    readLocalData()  //switch to room

        readLocalRealm()



        return binding.root
    }



    private fun readLocalRealm() {
        lifecycleScope.launch(Dispatchers.IO) {
        val db = Realm.getDefaultInstance()


Log.e("realmw",db.where(WeatherEntityRealm::class.java)
    .beginsWith("id","Am").findAll().maxOf { it.maxtempC?.toInt() ?: 0 } .toString())

        var testCopy= null


            var ammanResult= db.where(WeatherEntityRealm::class.java).equalTo("id",getString(R.string.Amman)).findFirst()
            var irbidResult= db.where(WeatherEntityRealm::class.java).equalTo("id",getString(R.string.Irbid)).findFirst()

            ammanResult = db.copyFromRealm(ammanResult!!)
            irbidResult = db.copyFromRealm(irbidResult!!)
            db.close()
            setAmmanWeather(ammanResult)
            setIrbidWeather(irbidResult)


}

    }


    private fun setIrbidWeather(database: WeatherEntityRealm?) {
        binding.cityName2.text = getString(R.string.Irbid)
        binding.minTemp2.text = database?.mintempC?: "-"
        binding.avgTemp2.text = database ?.avgtempC?: "-"
        binding.maxTemp2.text = database ?.maxtempC?: "-"
    }

    private fun setAmmanWeather(database: WeatherEntityRealm?) {
        binding.cityName.text = getString(R.string.Amman)
        binding.minTemp.text = database ?.mintempC?: "-"
        binding.avgTemp.text = database ?.avgtempC?: "-"
        binding.maxTemp.text = database ?.maxtempC?: "-"
    }

/*
     //switch to room
 private fun setIrbidWeather(database: WeatherEntity) {
        binding.cityName2.text = getString(R.string.Irbid)
        binding.minTemp2.text = database .mintempC
        binding.avgTemp2.text = database .avgtempC
        binding.maxTemp2.text = database .maxtempC
    }

    private fun setAmmanWeather(database: WeatherEntity) {
        binding.cityName.text = getString(R.string.Amman)
        binding.minTemp.text = database .mintempC
        binding.avgTemp.text = database .avgtempC
        binding.maxTemp.text = database .maxtempC
    }*/

}