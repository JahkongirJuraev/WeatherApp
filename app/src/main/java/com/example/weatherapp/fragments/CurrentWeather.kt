package com.example.weatherapp.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.weatherapp.R
import com.example.weatherapp.cache.Cache
import com.example.weatherapp.databinding.CurrentWaetherFragmentBinding
import com.example.weatherapp.network.connection.NetworkConnection
import com.example.weatherapp.network.const.CONSTANTS
import com.example.weatherapp.network.model.WeatherData
import com.example.weatherapp.network.service.WeatherService
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class CurrentWeather : Fragment() {

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

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        displayCurrentDate()

        loadDataFromNetwork()


    }

    private fun loadDataFromNetwork() {
        val connection = NetworkConnection.getRetrofit()
        val service: WeatherService = connection.create(WeatherService::class.java)
        val result = service.getCurrentWeather(
            "metric",
            Cache.getInstance().cityName,
            /*41.33838976977386,
            69.33081670755287,*/
            CONSTANTS.apiKey
        )

        result.enqueue(object : retrofit2.Callback<WeatherData> {
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

        })
    }

    private fun displayCurrentDate() {
        val formatter: SimpleDateFormat = SimpleDateFormat("d/MM/yyyy")
        val date: Date = Date()
        binding.currentDate.text = formatter.format(date)
    }


}