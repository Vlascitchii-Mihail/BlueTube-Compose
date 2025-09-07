package com.vlascitchii.presentation_common.ui.bottom_navigation

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vlascitchii.bluetubecompose.BlueTubeApp
import com.vlascitchii.bluetubecompose.MainActivity
import com.vlascitchii.presentation_common.ui.theme.BlueTubeComposeTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.vlascitchii.presentation_common.R as RCommon
import com.vlascitchii.presenttion_settings.R as RSettings

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class BlueTubeBottomNavigationTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeAndroidTestRule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity> =
        createAndroidComposeRule(MainActivity::class.java)

    @Before
    fun init_Activity() {
        composeAndroidTestRule.activity.setContent {
            BlueTubeComposeTheme {
                BlueTubeApp()
            }
        }
    }

    @Test
    fun bottomAppBar_selects_video_tab_on_start() {
        composeAndroidTestRule.run {
            val videoItemName = activity.getString(RCommon.string.video_list_screen)
            val shortsItemName = activity.getString(RCommon.string.shorts_screen)
            val settingsItemName = activity.getString(RCommon.string.settings_screen)

            onNodeWithText(videoItemName).assertIsSelected()
            onNodeWithText(shortsItemName).assertIsNotSelected()
            onNodeWithText(settingsItemName).assertIsNotSelected()
        }
    }

    @Test
    fun bottomAppBar_navigates_on_items_click() {
        composeAndroidTestRule.run {
            val settingsItemName = activity.getString(RCommon.string.settings_screen)

            onNodeWithText(settingsItemName).performClick()
            onNodeWithText(settingsItemName).assertIsSelected()
        }
    }
}
