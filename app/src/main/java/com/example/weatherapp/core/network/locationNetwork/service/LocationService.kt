package com.example.weatherapp.core.network.locationNetwork.service

import com.example.weatherapp.core.model.location.LocationResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationService {

    //reverse?lon=10&lat=52
    @GET("/reverse")
    fun getLocationData(
        @Query("lon") lon: Double,
        @Query("lat") lat: Double
    ): Single<Response<LocationResponse?>>
}