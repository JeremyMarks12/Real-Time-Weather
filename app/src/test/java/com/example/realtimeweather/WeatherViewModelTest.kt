package com.example.realtimeweather

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = StandardTestDispatcher(TestCoroutineScheduler())
    private lateinit var viewModel: WeatherViewModel

    @Mock
    private lateinit var observer: Observer<WeatherData>

    @Mock
    private lateinit var forecastObserver: Observer<List<DailyForecastItem>>

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        MockitoAnnotations.openMocks(this)
        viewModel = WeatherViewModel()
        viewModel.weather.observeForever(observer)
        viewModel.forecast.observeForever(forecastObserver)
    }

    @Test
    fun testSetZipForForecast() {
        viewModel.setZipForForecast("55344")
        viewModel.loadForecast()
    }

    @Test
    fun testSetCoordinatesForForecast() {
        viewModel.setCoordinatesForForecast(44.9, -93.4)
        viewModel.loadForecast()
    }

    @Test
    fun testFetchWeatherDoesNotCrash() = runTest(dispatcher) {
        viewModel.fetchWeather("55344,us")
    }

    @Test
    fun testFetchWeatherByCoordinatesDoesNotCrash() = runTest(dispatcher) {
        viewModel.fetchWeatherByCoordinates(44.9, -93.4)
    }

    @Test
    fun testFetchForecastDoesNotCrash() = runTest(dispatcher) {
        viewModel.fetchForecast("55344,us")
    }

    @Test
    fun testFetchForecastByCoordinatesDoesNotCrash() = runTest(dispatcher) {
        viewModel.fetchForecastByCoordinates(44.9, -93.4)
    }

    @Test
    fun testSetAndGetCurrentCoordinates() {
        viewModel.setCurrentCoordinates(44.9, -93.4)
        val (lat, lon) = viewModel.getCurrentCoordinates()
        assert(lat == 44.9 && lon == -93.4)
    }
}


