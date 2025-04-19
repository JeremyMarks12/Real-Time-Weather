package com.example.realtimeweather

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ForecastResponse(
    @SerialName("list") val list: List<ForecastItem>
)

@Serializable
data class ForecastItem(
    @SerialName("dt_txt") val dt_txt: String,
    @SerialName("main") val main: ForecastMain,
    @SerialName("weather") val weather: List<ForecastWeather>
)

@Serializable
data class ForecastMain(
    @SerialName("temp") val temp: Double
)

@Serializable
data class ForecastWeather(
    @SerialName("description") val description: String
)

