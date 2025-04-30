package com.example.realtimeweather

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

class WeatherViewModel : ViewModel() {

    private val _weather = MutableLiveData<WeatherData>()
    val weather: LiveData<WeatherData> = _weather

    private val _forecast = MutableLiveData<List<DailyForecastItem>>()
    val forecast: LiveData<List<DailyForecastItem>> = _forecast

    private val apiKey = "0789c32f478ef89e788a6df16db179c0"

    private var zipCode: String? = null
    private var coordinates: Pair<Double, Double>? = null

    fun setZipForForecast(zip: String) {
        zipCode = zip
        coordinates = null
    }

    fun setCoordinatesForForecast(lat: Double, lon: Double) {
        coordinates = Pair(lat, lon)
        zipCode = null
    }

    fun loadForecast() {
        if (!zipCode.isNullOrEmpty()) {
            fetchForecast(zipCode!!)
        } else if (coordinates != null) {
            fetchForecastByCoordinates(coordinates!!.first, coordinates!!.second)
        } else {
            Log.e("WeatherViewModel", "No valid location data to load forecast.")
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org/")
        .addConverterFactory(
            Json { ignoreUnknownKeys = true }
                .asConverterFactory("application/json".toMediaType())
        )
        .build()

    private val weatherApi = retrofit.create(WeatherAPI::class.java)

    fun fetchWeather(city: String) {
        viewModelScope.launch {
            try {
                val response = weatherApi.getCurrentWeather(cityName = city, apiKey = apiKey)
                _weather.postValue(response)
            } catch (e: Exception) {
                Log.e("WeatherViewModel", "Current weather API call failed: ${e.message}", e)
            }
        }
    }

    fun fetchWeatherByCoordinates(lat: Double, lon: Double) {
        viewModelScope.launch {
            try {
                val response = weatherApi.getCurrentWeatherByCoordinates(lat, lon, apiKey)
                _weather.postValue(response)
            } catch (e: Exception) {
                Log.e("WeatherViewModel", "Weather by coordinates failed: ${e.message}", e)
            }
        }
    }

    fun fetchForecast(zipCode: String) {
        viewModelScope.launch {
            try {
                val response = weatherApi.getDailyForecastByZip(zipCode = zipCode, apiKey = apiKey)
                _forecast.postValue(response.list)
            } catch (e: Exception) {
                Log.e("WeatherViewModel", "Forecast API call failed: ${e.message}", e)
            }
        }
    }

    fun fetchForecastByCoordinates(lat: Double, lon: Double) {
        viewModelScope.launch {
            try {
                val response = weatherApi.getDailyForecastByCoordinates(lat, lon, apiKey)
                _forecast.postValue(response.list)
            } catch (e: Exception) {
                Log.e("WeatherViewModel", "Forecast (lat/lon) API call failed: ${e.message}", e)
            }
        }
    }

    private var currentLat: Double? = null
    private var currentLon: Double? = null

    fun setCurrentCoordinates(lat: Double, lon: Double) {
        currentLat = lat
        currentLon = lon
    }

    fun getCurrentCoordinates(): Pair<Double?, Double?> {
        return Pair(currentLat, currentLon)
    }
}





