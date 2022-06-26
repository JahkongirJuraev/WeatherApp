package com.example.weatherapp.ui.currentWether

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.example.weatherapp.R
import com.example.weatherapp.core.cache.Cache
import com.example.weatherapp.core.model.WeatherData
import com.example.weatherapp.databinding.CurrentWaetherFragmentBinding
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*


class CurrentWeather : Fragment(), CurrentWeatherMVP.View {

    var presenter: CurrentWeatherMVP.Presenter? = null

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
        Cache.getInstance().cityName = Cache.getInstance().defaultCity
        presenter = CurrentWeatherPresenter(this)
        presenter?.loadCurrentWeather()
        loadDate()
        binding.searchView.isFocusable = false
        binding.activeWeatherInfo.visibility = View.INVISIBLE

        performSearch()

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
