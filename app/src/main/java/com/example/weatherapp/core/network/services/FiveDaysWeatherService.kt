package com.example.weatherapp.core.network.services

import com.example.weatherapp.core.model.FiveDaysWeatherResponse.FiveDaysHourlyWeatherRespond
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FiveDaysWeatherService {

    //https://api.openweathermap.org/data/2.5/forecast?q=Tashkent&appid=96eca76853b1592fe4a160df6ce70892&units=metric

    @GET("data/2.5/forecast")
    fun getFiveDaysWeather(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String
    ): Single<Response<FiveDaysHourlyWeatherRespond?>>
}