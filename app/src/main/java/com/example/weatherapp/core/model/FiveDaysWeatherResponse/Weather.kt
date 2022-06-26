package com.example.weatherapp.core.model.FiveDaysWeatherResponse

data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)