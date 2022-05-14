package com.example.weatherapp.network.connection

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkConnection {

    companion object {

        private var retrofit: Retrofit? = null

        //https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API key}
        //https://api.openweathermap.org/data/2.5/weather?q=Samarqand,Uzbekistan&appid=96eca76853b1592fe4a160df6ce70892
        fun getRetrofit(): Retrofit {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl("https://api.openweathermap.org")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit!!
        }
    }

}