package com.example.realtimeweather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.realtimeweather.ui.theme.RealTimeWeatherTheme

class MainActivity : ComponentActivity() {
    private val viewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RealTimeWeatherTheme {
                val navController = rememberNavController()
                WeatherNavigation(navController, viewModel)
            }
        }
    }
}

@Composable
fun WeatherNavigation(navController: NavHostController, viewModel: WeatherViewModel) {
    NavHost(navController = navController, startDestination = "current") {
        composable("current") {
            WeatherAppScreen(viewModel, navController)
        }
        composable("forecast/{zip}") { backStackEntry ->
            val zip = backStackEntry.arguments?.getString("zip") ?: ""
            ForecastScreen(viewModel, navController, zip)
        }
    }
}



