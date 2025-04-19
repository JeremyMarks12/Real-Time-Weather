package com.example.realtimeweather

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.compose.runtime.livedata.observeAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherAppScreen(viewModel: WeatherViewModel, navController: NavHostController) {
    val context = LocalContext.current
    val weatherState = viewModel.weather.observeAsState().value
    var zipCode by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.top_bar_title)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1E1E1E),
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = Color.Black
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = zipCode,
                    onValueChange = {
                        if (it.length <= 5 && it.all { c -> c.isDigit() }) zipCode = it
                    },
                    label = { Text(stringResource(R.string.enter_zip_code)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.Gray,
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.Gray,
                        cursorColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        if (zipCode.length == 5) {
                            Log.d("WeatherScreen", "Launching fetchWeather for $zipCode")
                            viewModel.fetchWeather("$zipCode,us")
                        } else {
                            Toast.makeText(context, context.getString(R.string.invalid_zip), Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
                ) {
                    Text(stringResource(R.string.get_weather), color = Color.White)
                }

                if (weatherState != null) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 80.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.city_label, weatherState.name, weatherState.sys.country),
                            fontSize = 20.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        val iconResId = getWeatherIconResource(weatherState.weather.firstOrNull()?.description ?: "")

                        Image(
                            painter = painterResource(id = iconResId),
                            contentDescription = stringResource(R.string.weather_icon),
                            modifier = Modifier.size(100.dp)
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "${weatherState.main.temp.toInt()}\u00B0",
                            fontSize = 48.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )

                        val condition = weatherState.weather.firstOrNull()?.description?.replaceFirstChar { it.uppercase() } ?: ""
                        Text(
                            text = condition,
                            fontSize = 18.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Normal
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = stringResource(R.string.low_label, weatherState.main.tempMin),
                                color = Color.White
                            )
                            Text(
                                text = stringResource(R.string.high_label, weatherState.main.tempMax),
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = stringResource(R.string.humidity_label, weatherState.main.humidity),
                                color = Color.White
                            )
                            Text(
                                text = stringResource(R.string.pressure_label, weatherState.main.pressure),
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(R.string.today_label),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                            TextButton(onClick = {
                                if (zipCode.length == 5) {
                                    viewModel.fetchForecast("$zipCode,us")
                                    navController.navigate("forecast/$zipCode")
                                } else {
                                    Toast.makeText(context, context.getString(R.string.invalid_zip), Toast.LENGTH_SHORT).show()
                                }
                            }) {
                                Text(stringResource(R.string.sixteen_day_label), color = Color.White)
                            }
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            repeat(4) {
                                Box(
                                    modifier = Modifier
                                        .size(60.dp)
                                        .background(Color.DarkGray),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("--\u00B0", color = Color.White)
                                }
                            }
                        }
                    }
                } else {
                    CircularProgressIndicator(color = Color.White)
                }
            }
        }
    }
}

fun getWeatherIconResource(description: String): Int {
    return when (description.lowercase()) {
        "clear sky" -> R.drawable.sunny
        "few clouds" -> R.drawable.fewclouds
        "scattered clouds" -> R.drawable.scatteredclouds
        "broken clouds" -> R.drawable.brokenclouds
        "light rain" -> R.drawable.showerrain
        "rain" -> R.drawable.rain
        "thunderstorm" -> R.drawable.thunderstorm
        "snow" -> R.drawable.snow
        "mist" -> R.drawable.mist
        "moderate rain" -> R.drawable.moderaterain
        else -> R.drawable.sunny
    }
}




