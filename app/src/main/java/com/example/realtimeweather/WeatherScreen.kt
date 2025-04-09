package com.example.realtimeweather

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.realtimeweather.ui.theme.RealTimeWeatherTheme
import androidx.compose.runtime.livedata.observeAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherAppScreen(viewModel: WeatherViewModel) {
    val weatherState = viewModel.weather.observeAsState().value

    LaunchedEffect(Unit) {
        Log.d("WeatherScreen", "Launching fetchWeather for 55406")
        viewModel.fetchWeather("55406,us")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Real Time Weather") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFB0B0B0)
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (weatherState != null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(85.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    // Centered city name
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${weatherState.name}, MN",
                            fontSize = 20.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 24.dp)
                        ) {
                            Text(
                                text = "${weatherState.main.temp.toInt()}°",
                                fontSize = 48.sp
                            )
                            Text(
                                text = "Feels like ${weatherState.main.feelsLike.toInt()}°",
                                fontSize = 16.sp
                            )
                        }

                        Image(
                            painter = painterResource(id = R.drawable.sunny),
                            contentDescription = "Weather Icon",
                            modifier = Modifier
                                .size(100.dp)
                                .padding(end = 32.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(text = "Low ${weatherState.main.tempMin.toInt()}°")
                    Text(text = "High ${weatherState.main.tempMax.toInt()}°")
                    Text(text = "Humidity ${weatherState.main.humidity}%")
                    Text(text = "Pressure ${weatherState.main.pressure} hPa")
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WeatherAppScreenPreview() {
    RealTimeWeatherTheme {
        WeatherAppScreen(viewModel = WeatherViewModel())
    }
}




