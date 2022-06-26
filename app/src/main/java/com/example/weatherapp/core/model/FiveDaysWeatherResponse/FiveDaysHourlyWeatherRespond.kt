package com.example.weatherapp.core.model.FiveDaysWeatherResponse

data class FiveDaysHourlyWeatherRespond(
    val city: City,
    val cnt: Float,
    val cod: String,
    val list: List<MoreData>,
    val message: Float
)