package com.example.realtimeweather

import android.util.Log // testing ran into issues.
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

    private val apiKey = "0789c32f478ef89e788a6df16db179c0"

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
                Log.e("WeatherViewModel", "API call failed: ${e.message}", e)
            }
        }
    }


}

