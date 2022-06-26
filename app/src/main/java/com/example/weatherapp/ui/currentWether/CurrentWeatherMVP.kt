package com.example.weatherapp.ui.currentWether

import com.example.weatherapp.core.model.WeatherData

interface CurrentWeatherMVP {

    interface View {

        fun getCurrentWeather(weatherData: WeatherData)

        fun onError(message: String)
    }

    interface Presenter {

        fun loadCurrentWeather()

        fun cancelRequest()
    }
}