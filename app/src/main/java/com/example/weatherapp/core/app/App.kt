package com.example.weatherapp.core.app

import android.app.Application
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.core.cache.Cache
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        Cache.init(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }


    companion object {
        var instance: Application? = null
            private set
    }
}