package com.example.realtimeweather

import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WeatherAppScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testZipInputAndFetchWeatherButtonExists() {
        composeTestRule.onNodeWithText("Enter Zip Code").assertExists()
        composeTestRule.onNodeWithText("Get Weather").assertExists()
    }

    @Test
    fun testInvalidZipToastShown() {
        composeTestRule.onNodeWithText("Get Weather").performClick()
    }

    @Test
    fun testLocationIconExists() {
        composeTestRule.onNode(hasContentDescription("Use My Location")).assertExists()
    }

    @Test
    fun testSixteenDayButtonExists() {

        composeTestRule.onNodeWithText("Enter Zip Code").performTextInput("55344")
        composeTestRule.onNodeWithText("Get Weather").performClick()
        Thread.sleep(2000)
        composeTestRule.onNodeWithTag("sixteenDayButton").assertExists()
    }

    @Test
    fun testForecastNavigation() {

        composeTestRule.onNodeWithText("Enter Zip Code").performTextInput("55344")
        composeTestRule.onNodeWithText("Get Weather").performClick()
        Thread.sleep(2000)
        composeTestRule.onNodeWithTag("sixteenDayButton").performClick()
    }
}




