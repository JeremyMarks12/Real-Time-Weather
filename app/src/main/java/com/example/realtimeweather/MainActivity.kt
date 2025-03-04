package com.example.realtimeweather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.realtimeweather.ui.theme.RealTimeWeatherTheme

import androidx.compose.foundation.layout.fillMaxWidth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherAppScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.top_bar_title)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Gray
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = stringResource(id = R.string.city_name), fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 75.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = stringResource(id = R.string.temperature), fontSize = 48.sp, fontWeight = FontWeight.Bold)
                    Text(text = stringResource(id = R.string.feels_like), fontSize = 16.sp)
                }
                Box(
                    modifier = Modifier.padding(end = 100.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.sunny),
                        contentDescription = "Weather Icon",
                        modifier = Modifier.size(65.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier.padding(start = 75.dp)
            ) {
                Text(text = stringResource(id = R.string.low_temp), fontSize = 16.sp)
                Text(text = stringResource(id = R.string.high_temp), fontSize = 16.sp)
                Text(text = stringResource(id = R.string.humidity), fontSize = 16.sp)
                Text(text = stringResource(id = R.string.pressure), fontSize = 16.sp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WeatherAppScreenPreview() {
    RealTimeWeatherTheme {
        WeatherAppScreen()
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RealTimeWeatherTheme {
        Greeting("Android")
    }
}