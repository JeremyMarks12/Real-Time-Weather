package com.example.realtimeweather

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DailyForecastResponse(
    @SerialName("list") val list: List<DailyForecastItem>
)

@Serializable
data class DailyForecastItem(
    @SerialName("dt") val dt: Long,
    @SerialName("temp") val temp: DailyTemp,
    @SerialName("weather") val weather: List<DailyWeather>
)

@Serializable
data class DailyTemp(
    @SerialName("min") val min: Double,
    @SerialName("max") val max: Double
)

@Serializable
data class DailyWeather(
    @SerialName("description") val description: String,
    @SerialName("icon") val icon: String
)

