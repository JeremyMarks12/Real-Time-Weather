package com.example.realtimeweather

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {
    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String,
        @Query("units") unit: String = "imperial"
    ): WeatherData

    @GET("data/2.5/forecast/daily")
    suspend fun getDailyForecastByZip(
        @Query("zip") zipCode: String,
        @Query("appid") apiKey: String,
        @Query("cnt") cnt: Int = 16,
        @Query("units") unit: String = "imperial"
    ): DailyForecastResponse
}



