package com.appelier.bluetubecompose

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import com.appelier.bluetubecompose.utils.Tags
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class BlueTubeNavigationTest {

    @get:Rule(order = 0)
    val composeTestRule = createAndroidComposeRule(MainActivity::class.java)

    @Before
    fun init_app() {
        composeTestRule.activity.setContent {
            BlueTubeApp(navController = rememberNavController())
        }
    }

    @Test
    fun app_shows_navigation_and_bottom_nav() {
        composeTestRule.onNodeWithTag(Tags.NAVIGATION).assertIsDisplayed()
        composeTestRule.onNodeWithTag(Tags.BOTTOM_NAV).assertIsDisplayed()
    }

    @Test
    fun app_shows_button_nav_navigates_to_screens() {
        composeTestRule.onNodeWithTag(Tags.VIDEO_LIST_SCREEN).assertIsDisplayed()
        composeTestRule.onNodeWithText("Shorts").performClick()
        composeTestRule.onNodeWithTag(Tags.SHORTS_SCREEN).assertIsDisplayed()
        composeTestRule.onNodeWithText("Settings").performClick()
        composeTestRule.onNodeWithTag(Tags.SETTINGS_SCREEN).assertIsDisplayed()
        composeTestRule.activityRule.scenario.onActivity { activity ->
            activity.onBackPressedDispatcher.onBackPressed()
        }
        composeTestRule.onNodeWithTag(Tags.VIDEO_LIST_SCREEN).assertIsDisplayed()
    }
}