package com.example.weatherapp

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.example.weatherapp.cache.Cache
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.network.model.LocationData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.net.URLConnection

class MainActivity : AppCompatActivity() {

    var _binding: ActivityMainBinding? = null
    val binding get() = _binding!!
    lateinit var s: String

/*
    val navigationComponent = Navigation.findNavController(this,R.id.nav_host_fragment)
    binding.mainBottomView.setupWithNavController(Navigation.findNavController(this,R.id.nav_host_fragment))*/


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val navigationComponent = Navigation.findNavController(this, R.id.nav_host_fragment)
        binding.mainBottomView.setupWithNavController(
            Navigation.findNavController(
                this,
                R.id.nav_host_fragment
            )
        )

        requestPermission()


    }

    val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                // Precise location access granted.
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // Only approximate location access granted.
            }
            else -> {
                // No location access granted.
            }
        }
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    fun requestPermission() {


        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionRequest.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        } else {
            Toast.makeText(this, "Granted ", Toast.LENGTH_SHORT).show()

            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->

                    location?.let {
                        val lon: Double = location.longitude
                        val lat: Double = location.latitude
                        val url: String =
                            "https://nominatim.openstreetmap.org/reverse?lat=$lat&lon=$lon&zoom18&format=json"
                        val content: String = getContentFromUsl(url)
                        val gson: Gson = Gson()
                        val data: LocationData = gson.fromJson(content, LocationData::class.java)
                        var stringBuilder: String = data.address.cityName
                        Cache.getInstance().cityName = stringBuilder
                    }

                }
        }


    }

    fun getContentFromUsl(stringUrl: String): String {
        var stringBuilder = StringBuilder()
        try {
            var url = URL(stringUrl)

            var urlConnection: URLConnection = url.openConnection()

            var bufferReader: BufferedReader =
                BufferedReader(InputStreamReader(urlConnection.getInputStream()))

            while ((bufferReader.readLine().also { s = it }) != null) {
                stringBuilder.append(s)
            }

            bufferReader.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return stringBuilder.toString()
    }
}


