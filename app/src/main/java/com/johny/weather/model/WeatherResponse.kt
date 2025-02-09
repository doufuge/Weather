package com.johny.weather.model

import java.io.Serializable

data class WeatherResponse(
    val latitude: Double,
    val longitude: Double,
    val generationtime_ms: Double,
    val utc_offset_seconds: Int,
    val timezone: String,
    val timezone_abbreviation: String,
    val elevation: Int,
    val hourly_units: HourlyUnits,
    val hourly: Hourly,
) : Serializable {

    data class HourlyUnits(
        val time: String,
        val temperature_2m: String,
    )

    data class Hourly(
        val time: List<String>,
        val temperature_2m: List<Float>,
    )

}
