package com.example.realtimeweather

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherData(
    @SerialName("name") val name: String,
    @SerialName("main") val main: Main,
    @SerialName("weather") val weather: List<WeatherDescription>,
    @SerialName("sys") val sys: Sys
)

@Serializable
data class Main(
    @SerialName("temp") val temp: Double,
    @SerialName("feels_like") val feelsLike: Double,
    @SerialName("temp_min") val tempMin: Double,
    @SerialName("temp_max") val tempMax: Double,
    @SerialName("humidity") val humidity: Int,
    @SerialName("pressure") val pressure: Int
)


@Serializable
data class WeatherDescription(
    @SerialName("main") val main: String,
    @SerialName("description") val description: String,
    @SerialName("icon") val icon: String
)

@Serializable
data class Sys(
    @SerialName("country") val country: String
)

