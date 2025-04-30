package com.example.realtimeweather

import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ForecastScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private fun ComposeTestRule.navigateToForecastScreen() {
        onNodeWithTag("zipInputField").performTextInput("55344")
        onNodeWithText("Get Weather").performClick()

        waitUntil(timeoutMillis = 5_000) {
            onAllNodesWithTag("sixteenDayButton").fetchSemanticsNodes().isNotEmpty()
        }

        onNodeWithTag("sixteenDayButton").performClick()

        waitUntil(timeoutMillis = 5_000) {
            onAllNodesWithTag("forecastTitle").fetchSemanticsNodes().isNotEmpty()
        }
    }

    @Test
    fun testForecastTopBarDisplayed() {
        composeTestRule.navigateToForecastScreen()
        composeTestRule.onNodeWithTag("forecastTitle").assertExists()
    }

    @Test
    fun testForecastListRenders() {
        composeTestRule.navigateToForecastScreen()
        composeTestRule.waitUntilExists(hasContentDescription("Weather Icon"))
        composeTestRule.onAllNodesWithContentDescription("Weather Icon").fetchSemanticsNodes()
    }

    @Test
    fun testBackButtonExists() {
        composeTestRule.navigateToForecastScreen()
        composeTestRule.onNodeWithTag("backButton").assertExists()
    }


    private fun ComposeTestRule.waitUntilExists(
        matcher: androidx.compose.ui.test.SemanticsMatcher,
        timeoutMillis: Long = 3_000L
    ) {
        waitUntil(timeoutMillis) {
            onAllNodes(matcher).fetchSemanticsNodes().isNotEmpty()
        }
    }
}










