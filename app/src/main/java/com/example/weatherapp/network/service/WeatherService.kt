package com.example.weatherapp.network.service

import com.example.weatherapp.network.model.WeatherData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    //https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API key}
    //https://api.openweathermap.org/
    // data/2.5/weather?q=Samarqand,Uzbekistan&appid=96eca76853b1592fe4a160df6ce70892

    @GET("data/2.5/weather")
    fun getCurrentWeather(
        @Query("units") unit: String,
        /*@Query("lat") lt:Double,
        @Query("lon") ln:Double,*/
        @Query("q") cityName: String,
        @Query("appid") apiKey: String
    ): Call<WeatherData>

}