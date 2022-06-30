package com.example.weatherapp.core.model.location

data class Feature(
    val geometry: Geometry,
    val properties: Properties,
    val type: String
)