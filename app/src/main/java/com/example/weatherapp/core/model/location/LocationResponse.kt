package com.example.weatherapp.core.model.location

data class LocationResponse(
    val features: List<Feature>,
    val type: String
)