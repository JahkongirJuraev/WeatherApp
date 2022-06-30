package com.example.weatherapp.ui.currentWether

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.weatherapp.R
import com.example.weatherapp.core.cache.Cache
import com.example.weatherapp.core.model.WeatherData
import com.example.weatherapp.core.model.location.LocationResponse
import com.example.weatherapp.core.network.locationNetwork.LocationApiClientModule
import com.example.weatherapp.databinding.CurrentWaetherFragmentBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class CurrentWeather : Fragment(), CurrentWeatherMVP.View {

    var presenter: CurrentWeatherMVP.Presenter? = null
    var locationService = LocationApiClientModule.getLocationService()
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var _binding: CurrentWaetherFragmentBinding? = null
    val binding get() = _binding!!
    var weatherData: WeatherData? = null
    var feelsLike: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CurrentWaetherFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (Cache.getInstance().locationGranted) {
            presenter = CurrentWeatherPresenter(this)
            presenter?.loadCurrentWeather()
        } else {
            getUserLocation()
        }
        Cache.getInstance().cityName = Cache.getInstance().defaultCity
        loadDate()

        binding.searchView.isFocusable = false
        binding.activeWeatherInfo.visibility = View.INVISIBLE

        performSearch()

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getUserLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        requestPermission()
    }


    @RequiresApi(Build.VERSION_CODES.N)
    private fun requestPermission() {


        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                requireContext(),
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
            Toast.makeText(requireContext(), "Granted ", Toast.LENGTH_SHORT).show()

            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->

                    location?.let {
                        val lon: Double = location.longitude
                        val lat: Double = location.latitude
                        var disposable = locationService.getLocationData(lon = lon, lat = lat)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribeWith(
                                object :
                                    DisposableSingleObserver<Response<LocationResponse?>>() {
                                    override fun onSuccess(t: Response<LocationResponse?>) {
                                        if (t.isSuccessful) {
                                            t.body()?.let {
                                                Cache.getInstance().cityName =
                                                    it.features[0].properties.city
                                                Cache.getInstance().defaultCity =
                                                    it.features[0].properties.city
                                                Cache.getInstance().locationGranted = true
                                                startPresenter()
                                            }
                                        } else {
                                            Snackbar.make(
                                                requireContext(),
                                                binding.root,
                                                t.message().toString() + " TOP  ",
                                                Snackbar.LENGTH_SHORT
                                            ).show()
                                        }
                                    }

                                    override fun onError(e: Throwable) {
                                        Snackbar.make(
                                            requireContext(),
                                            binding.root,
                                            e.message.toString() + "  Bottom ",
                                            Snackbar.LENGTH_SHORT
                                        ).show()
                                    }

                                })
                        /*Log.d("TAG",lat.toString())
                        Log.d("TAG",lon.toString())
                        val url: String =
                            "https://nominatim.openstreetmap.org/reverse?lat=$lat&lon=$lon&zoom18&format=json"
                        val content: String = getContentFromUsl(url)
                        val gson: Gson = Gson()
                        // val data: LocationData = gson.fromJson(content, LocationData::class.java)
                        //var stringBuilder: String = data.address.cityName
                        //Cache.getInstance().cityName = stringBuilder*/
                    }

                }
        }

    }

    private fun startPresenter() {
        presenter = CurrentWeatherPresenter(this)
        presenter?.loadCurrentWeather()
    }

    @RequiresApi(Build.VERSION_CODES.N)
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
    }


    private fun performSearch() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                Cache.getInstance().cityName = query?.trim()
                presenter?.loadCurrentWeather()

                binding.searchView.clearFocus()

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                return true
            }
        })
    }

    private fun loadDate() {
        val formatter: SimpleDateFormat = SimpleDateFormat("d/MM/yyyy")
        val date: Date = Date()
        binding.currentDate.text = formatter.format(date)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.cancelRequest()
    }

    override fun getCurrentWeather(weatherData: WeatherData) {

        binding.activeWeatherInfo.visibility = View.VISIBLE
        binding.shimmerLayout.stopShimmer()
        binding.shimmerLayout.visibility = View.INVISIBLE

        binding.currentLocation.text = weatherData.name
        feelsLike = weatherData.main.feels_like.toInt()
        binding.feelsLike.text = feelsLike.toString() + "°"
        binding.weatherDescription.text = weatherData.weather[0].description

        if (weatherData.weather[0].main == "Thunderstorm") {
            binding.weatherMainImage.setImageResource(R.drawable.thunderstone)
            binding.weatherIcon.setImageResource(R.drawable.thunderstoneicon)
        } else if (weatherData.weather[0].main == "Drizzle") {
            binding.weatherMainImage.setImageResource(R.drawable.rain1)
            binding.weatherIcon.setImageResource(R.drawable.heavyrainicon)
        } else if (weatherData.weather[0].main == "Rain") {
            binding.weatherMainImage.setImageResource(R.drawable.rain2)
            binding.weatherIcon.setImageResource(R.drawable.rainicon)
        } else if (weatherData.weather[0].main == "Snow") {
            binding.weatherMainImage.setImageResource(R.drawable.snow)
            binding.weatherIcon.setImageResource(R.drawable.snowicon)
        } else if (weatherData.weather[0].id in 701..798) {
            binding.weatherMainImage.setImageResource(R.drawable.mist)
            binding.weatherIcon.setImageResource(R.drawable.misticon)
        } else if (weatherData.weather[0].main == "Clear") {
            binding.weatherMainImage.setImageResource(R.drawable.clear_sky_background)
            binding.weatherIcon.setImageResource(R.drawable.clear_sky_icon)
        } else if (weatherData.weather[0].id == 801 || weatherData.weather.get(0).id == 802) {
            binding.weatherMainImage.setImageResource(R.drawable.fewclouds)
            binding.weatherIcon.setImageResource(R.drawable.littlecloudicon)
        } else if (weatherData.weather[0].id == 803 || weatherData.weather.get(0).id == 804) {
            binding.weatherMainImage.setImageResource(R.drawable.fewclouds)
            binding.weatherIcon.setImageResource(R.drawable.littlecloudicon)
        }
    }

    override fun onError(message: String) {
        if (message == "Not Found") {
            Snackbar.make(
                requireContext(),
                binding.root,
                "Please enter valid city name",
                Snackbar.LENGTH_SHORT
            ).show()
            Cache.getInstance().cityName = Cache.getInstance().defaultCity
        } else {
            Snackbar.make(requireContext(), binding.root, message, Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onStop() {
        super.onStop()
        Cache.getInstance().cityName = Cache.getInstance().defaultCity
    }


}


/* result.enqueue(object : retrofit2.Callback<WeatherData> {
          override fun onResponse(
              call: Call<WeatherData>,
              response: Response<WeatherData>
          ) {
              if (response.isSuccessful) {
                  val data = response.body()
                  Log.d("TAG", "onResponse: $data")
                  weatherData = data!!
                  binding.currentLocation.text = weatherData!!.name
                  feelsLike = weatherData!!.main.feels_like.toInt()
                  binding.feelsLike.text = feelsLike.toString() + "°"
                  binding.weatherDescription.text = weatherData!!.weather[0].description

                  if (weatherData!!.weather[0].main == "Thunderstorm") {
                      binding.weatherMainImage.setImageResource(R.drawable.thunderstone)
                      binding.weatherIcon.setImageResource(R.drawable.thunderstoneicon)
                  } else if (weatherData!!.weather[0].main == "Drizzle") {
                      binding.weatherMainImage.setImageResource(R.drawable.rain1)
                      binding.weatherIcon.setImageResource(R.drawable.heavyrainicon)
                  } else if (weatherData!!.weather[0].main == "Rain") {
                      binding.weatherMainImage.setImageResource(R.drawable.rain2)
                      binding.weatherIcon.setImageResource(R.drawable.rainicon)
                  } else if (weatherData!!.weather[0].main == "Snow") {
                      binding.weatherMainImage.setImageResource(R.drawable.snow)
                      binding.weatherIcon.setImageResource(R.drawable.snowicon)
                  } else if (weatherData!!.weather[0].id in 701..798) {
                      binding.weatherMainImage.setImageResource(R.drawable.mist)
                      binding.weatherIcon.setImageResource(R.drawable.misticon)
                  } else if (weatherData!!.weather[0].main == "Clear") {
                      binding.weatherMainImage.setImageResource(R.drawable.clear_sky_background)
                      binding.weatherIcon.setImageResource(R.drawable.clear_sky_icon)
                  } else if (weatherData!!.weather[0].id == 801 || weatherData!!.weather.get(0).id == 802) {
                      binding.weatherMainImage.setImageResource(R.drawable.fewclouds)
                      binding.weatherIcon.setImageResource(R.drawable.littlecloudicon)
                  } else if (weatherData!!.weather[0].id == 803 || weatherData!!.weather.get(0).id == 804) {
                      binding.weatherMainImage.setImageResource(R.drawable.fewclouds)
                      binding.weatherIcon.setImageResource(R.drawable.littlecloudicon)
                  }


              }
          }

          override fun onFailure(call: Call<WeatherData>, t: Throwable) {
              Log.d("TAG", "onFailure: ${t}")
              Toast.makeText(requireActivity(), "${t.message}", Toast.LENGTH_SHORT).show()
          }

      })*/
