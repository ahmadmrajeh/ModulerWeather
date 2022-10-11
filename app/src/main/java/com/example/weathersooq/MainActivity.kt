package com.example.weathersooq

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.weathersooq.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

       binding.bottomNavigation.visibility= View.INVISIBLE

        setContentView(binding.root)

        navController= Navigation.findNavController(this,R.id.fragment)
        setupWithNavController(binding.bottomNavigation,navController)
         binding.bottomNavigation.visibility= View.INVISIBLE


    }

}

