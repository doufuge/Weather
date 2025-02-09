package com.johny.weather.model

data class WeatherItem(
    val hour: String,
    val temp: Float,
    val tempUnit: String = "°C"
)