package com.example.weatherapp.app

import android.app.Application
import com.example.weatherapp.cache.Cache

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Cache.init(this)
    }
}