package com.example.weatherapp.core.network.locationNetwork

import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import com.example.weatherapp.core.app.App
import com.example.weatherapp.core.network.locationNetwork.service.LocationService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

object LocationApiClientModule {

    private fun getHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor {
            Timber.d("com.example.weatherapp ## %s", it)
        }
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    /*private fun interceptor(): Interceptor {
        return Interceptor { chain: Interceptor.Chain ->
            val request = chain.request()
            val builder: Request.Builder = request.newBuilder()
            builder.header("Connection", "close")
                .addHeader("Content-Type", "application/json")
            val response = chain.proceed(builder.build())
            response
        }
    }*/

    private fun getOkHttpClient(): OkHttpClient {
        return OkHttpClient()
            .newBuilder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .addInterceptor(getHttpLoggingInterceptor())
            .addInterceptor(getChuckerInterceptor())
            //.addInterceptor(interceptor())
            .build()
    }

    private fun getChuckerCollection(): ChuckerCollector {
        return ChuckerCollector(
            context = App.instance!!,
            showNotification = true,
            retentionPeriod = RetentionManager.Period.FOREVER
        )
    }

    private fun getChuckerInterceptor(): ChuckerInterceptor {
        return ChuckerInterceptor.Builder(App.instance!!)
            .maxContentLength(250_000L)
            .alwaysReadResponseBody(true)
            .build()
    }

    private fun getApiClient(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://photon.komoot.io")
            .client(getOkHttpClient())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(getGson()))
            .build()
    }

    private fun getGson(): Gson = GsonBuilder().setLenient().create()

    fun getLocationService(): LocationService {
        return LocationApiClientModule.getApiClient().create(LocationService::class.java)
    }


}