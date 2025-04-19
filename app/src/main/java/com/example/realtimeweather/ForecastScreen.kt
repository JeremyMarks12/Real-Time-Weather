package com.example.realtimeweather

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForecastScreen(viewModel: WeatherViewModel, navController: NavController, zipCode: String) {
    LaunchedEffect(zipCode) {
        viewModel.fetchForecast(zipCode)
    }

    val forecastList = viewModel.forecast.observeAsState(emptyList()).value

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.forecast_screen_title)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.forecast_screen_title),
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1E1E1E),
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = Color.Black
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(forecastList) { item ->
                ForecastItemView(item)
            }
        }
    }
}

@Composable
fun ForecastItemView(item: DailyForecastItem) {
    val date = rememberFormattedDate(item.dt)
    val iconResId = getWeatherIconResource(item.weather.firstOrNull()?.description ?: "")
    val description = item.weather.firstOrNull()?.description ?: "--"

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.DarkGray)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = iconResId),
            contentDescription = stringResource(R.string.weather_icon),
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = date,
                fontSize = 16.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(R.string.condition_label, description),
                fontSize = 14.sp,
                color = Color.White
            )
            Text(
                text = stringResource(R.string.temp_range, item.temp.max.toInt(), item.temp.min.toInt()),
                fontSize = 14.sp,
                color = Color.White
            )
        }
    }
}

@Composable
fun rememberFormattedDate(timestamp: Long): String {
    val date = Date(timestamp * 1000)
    val formatter = SimpleDateFormat("EEE, MMM d", Locale.getDefault())
    return formatter.format(date)
}











