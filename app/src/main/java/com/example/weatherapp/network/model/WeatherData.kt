package com.example.weatherapp.network.model

class WeatherData(
    var name: String,
    val weather: List<Weather>,
    val cord: Cord,
    val main: Main,
    val wind: Wind,
    /*val sys:List<sys>*/
)

class Cord(
    val lon: Double,
    val lat: Double
)

class Weather(
    val main: String,
    val description: String,
    //val icon:String
    val id: Int
)

class sys(
    val country: String
)

class Main(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Double
)

class Wind(
    val wind: Double
)

