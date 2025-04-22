package com.example.realtimeweather

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.realtimeweather.ui.theme.RealTimeWeatherTheme

class MainActivity : ComponentActivity() {

    private val viewModel: WeatherViewModel by viewModels()
    private var locationService: LocationWeatherService? = null
    private var isBound = false

    private val locationPermissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val locationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
            val notificationGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                permissions[Manifest.permission.POST_NOTIFICATIONS] == true
            } else true

            if (locationGranted && notificationGranted) {
                startLocationWeatherService()
            }
        }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as? LocationWeatherService.LocalBinder
            locationService = binder?.getService()
            isBound = true

            locationService?.getLastKnownLocation { location ->
                viewModel.fetchWeatherByCoordinates(location.latitude, location.longitude)
                viewModel.setCurrentCoordinates(location.latitude, location.longitude)
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            locationService = null
            isBound = false
        }
    }

    private fun requestPermissionsAndBindService() {
        val permissions = mutableListOf(Manifest.permission.ACCESS_FINE_LOCATION)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.POST_NOTIFICATIONS)
        }
        locationPermissionRequest.launch(permissions.toTypedArray())
    }

    private fun startLocationWeatherService() {
        val intent = Intent(this, LocationWeatherService::class.java)
        startService(intent)
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RealTimeWeatherTheme {
                val navController = rememberNavController()
                WeatherNavigation(
                    navController = navController,
                    viewModel = viewModel,
                    locationService = locationService,
                    onRequestLocation = { requestPermissionsAndBindService() }
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isBound) {
            unbindService(serviceConnection)
            isBound = false
        }
    }
}

@Composable
fun WeatherNavigation(
    navController: NavHostController,
    viewModel: WeatherViewModel,
    locationService: LocationWeatherService?,
    onRequestLocation: () -> Unit
) {
    NavHost(navController = navController, startDestination = "current") {
        composable("current") {
            WeatherAppScreen(
                viewModel = viewModel,
                navController = navController,
                locationService = locationService,
                onRequestLocation = onRequestLocation
            )
        }
        composable("forecast") {
            viewModel.loadForecast()
            ForecastScreen(viewModel, navController, "")
        }
    }
}











