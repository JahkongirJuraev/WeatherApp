package com.example.weatherapp.ui


import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.example.weatherapp.R
import com.example.weatherapp.core.extension.SetItemStatusBarColor
import com.example.weatherapp.core.network.locationNetwork.LocationApiClientModule
import com.example.weatherapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {


    var _binding: ActivityMainBinding? = null
    val binding get() = _binding!!
    lateinit var s: String
    var locationService = LocationApiClientModule.getLocationService()

/*
    val navigationComponent = Navigation.findNavController(this,R.id.nav_host_fragment)
    binding.mainBottomView.setupWithNavController(Navigation.findNavController(this,R.id.nav_host_fragment))*/


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        SetItemStatusBarColor(getColor(R.color.white), true)

        //1/ Cache.getInstance().cityName = Cache.getInstance().defaultCity

        //1/fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val navigationComponent = Navigation.findNavController(this, R.id.nav_host_fragment)
        binding.mainBottomView.setupWithNavController(
            Navigation.findNavController(
                this,
                R.id.nav_host_fragment
            )
        )

        //1/requestPermission()


    }

    /*//1/@RequiresApi(Build.VERSION_CODES.N)
    val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                requestPermission()
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                requestPermission()

            }
            else -> {
                // No location access granted.
            }
        }
    }*/

    //1/private lateinit var fusedLocationClient: FusedLocationProviderClient

    /*//1/@RequiresApi(Build.VERSION_CODES.N)
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
                        var disposable = locationService.getLocationData(lon = lon, lat = lat)
                            .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                            .subscribeWith(
                                object : DisposableSingleObserver<Response<LocationResponse?>>() {
                                    override fun onSuccess(t: Response<LocationResponse?>) {
                                        if (t.isSuccessful) {
                                            t.body()?.let {
                                                Cache.getInstance().cityName=it.features[0].properties.city
                                                Cache.getInstance().defaultCity=it.features[0].properties.city
                                            }
                                        } else {
                                            Snackbar.make(
                                                applicationContext,
                                                binding.root,
                                                t.message().toString(),
                                                Snackbar.LENGTH_SHORT
                                            ).show()
                                        }
                                    }

                                    override fun onError(e: Throwable) {
                                        Snackbar.make(
                                            applicationContext,
                                            binding.root,
                                            e.message.toString(),
                                            Snackbar.LENGTH_SHORT
                                        ).show()
                                    }

                                })
                        *//*Log.d("TAG",lat.toString())
                        Log.d("TAG",lon.toString())
                        val url: String =
                            "https://nominatim.openstreetmap.org/reverse?lat=$lat&lon=$lon&zoom18&format=json"
                        val content: String = getContentFromUsl(url)
                        val gson: Gson = Gson()
                        // val data: LocationData = gson.fromJson(content, LocationData::class.java)
                        //var stringBuilder: String = data.address.cityName
                        //Cache.getInstance().cityName = stringBuilder*//*
                    }

                }
        }*/


}

/*fun getContentFromUsl(stringUrl: String): String {
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
}*/
//}


