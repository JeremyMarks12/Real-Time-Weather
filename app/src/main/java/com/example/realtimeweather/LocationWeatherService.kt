package com.example.realtimeweather

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.location.Location
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

class LocationWeatherService : Service() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val binder = LocalBinder()

    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    private lateinit var weatherApi: WeatherAPI
    private val apiKey = "0789c32f478ef89e788a6df16db179c0"

    inner class LocalBinder : Binder() {
        fun getService(): LocationWeatherService = this@LocationWeatherService
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        NotificationHelp.createNotificationChannel(this)
        setupWeatherApi()
        startForegroundNotification()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    fun getLastKnownLocation(callback: (Location) -> Unit) {
        val permission = android.Manifest.permission.ACCESS_FINE_LOCATION
        if (androidx.core.content.ContextCompat.checkSelfPermission(this, permission)
            != android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            Log.w("LocationService", "Location permission not granted.")
            return
        }

        try {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        callback(location)
                    } else {
                        Log.w("LocationService", "No last known location available.")
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("LocationService", "Unable to get location: ${e.message}", e)
                }
        } catch (e: SecurityException) {
            Log.e("LocationService", "SecurityException while accessing location: ${e.message}", e)
        }
    }

    private fun setupWeatherApi() {
        val contentType = "application/json".toMediaType()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(Json { ignoreUnknownKeys = true }.asConverterFactory(contentType))
            .build()

        weatherApi = retrofit.create(WeatherAPI::class.java)
    }

    private fun startForegroundNotification() {
        getLastKnownLocation { location ->
            scope.launch {
                try {
                    val response = weatherApi.getCurrentWeatherByCoordinates(
                        latitude = location.latitude,
                        longitude = location.longitude,
                        apiKey = apiKey
                    )

                    // Use shared notification helper to build and show it
                    NotificationHelp.showWeatherNotification(this@LocationWeatherService, response)

                    // Retrieve the built notification from NotificationManager
                    val notification = NotificationCompat.Builder(this@LocationWeatherService, "weather_channel")
                        .setContentTitle(getString(R.string.notification_title))
                        .setContentText(
                            getString(
                                R.string.notification_content,
                                response.name,
                                response.main.temp,
                                response.weather.firstOrNull()?.description ?: "--"
                            )
                        )
                        .setSmallIcon(getWeatherIconResource(response.weather.firstOrNull()?.description ?: ""))
                        .setOngoing(true)
                        .setPriority(NotificationCompat.PRIORITY_LOW)
                        .setContentIntent(PendingIntent.getActivity(
                            this@LocationWeatherService,
                            0,
                            Intent(this@LocationWeatherService, MainActivity::class.java).apply {
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            },
                            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                        ))
                        .build()

                    startForeground(1, notification)

                } catch (e: Exception) {
                    Log.e("LocationService", "Failed to fetch weather for notification: ${e.message}", e)
                }
            }
        }
    }
}









