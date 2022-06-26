package com.example.weatherapp.ui.fiveDaysWeather

import com.example.weatherapp.core.model.FiveDaysWeatherResponse.FiveDaysHourlyWeatherRespond

interface FiveDaysWeatherMVP {

    interface View {

        fun getFiveDaysWeather(fiveDaysHourlyWeatherRespond: FiveDaysHourlyWeatherRespond?)

        fun onError(message: String)

    }

    interface Presenter {

        fun loadFiveDaysWeather()

        fun cancelRequest()

    }

}