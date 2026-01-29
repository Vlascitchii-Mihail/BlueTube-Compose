package com.vlascitchii.bluetubecompose.navigation.bottom_navigation

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vlascitchii.bluetubecompose.MainActivity
import com.vlascitchii.bluetubecompose.navigation.ScreenType
import com.vlascitchii.presentation_common.ui.theme.BlueTubeComposeTheme
import com.vlascitchii.presentation_shorts.screen_shorts.screen.SHORTS_BOTTOM_NAV_NAME
import com.vlascitchii.presentation_video_list.screen.VIDEO_LIST_BOTTOM_NAV_NAME
import com.vlascitchii.presenttion_settings.screen_settings.SETTINGS_BOTTOM_NAV_NAME
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.any
import org.mockito.kotlin.never
import org.mockito.kotlin.spy
import org.mockito.kotlin.verify

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class BlueTubeBottomNavigationTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeAndroidTestRule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity> =
        createAndroidComposeRule(MainActivity::class.java)

    private val videoListScreen: String = VIDEO_LIST_BOTTOM_NAV_NAME
    private val shortsScreen: String = SHORTS_BOTTOM_NAV_NAME
    private val settingsScreen: String = SETTINGS_BOTTOM_NAV_NAME
    private lateinit var backStack: NavBackStack<NavKey>

    @Before
    fun init_Activity() {
        with(composeAndroidTestRule.activity) {
            backStack = spy(NavBackStack(ScreenType.VideoList()))

            setContent {
                BlueTubeComposeTheme {
                    BlueTubeBottomNavigation(backStack = backStack)
                }
            }
        }
    }

    @Test
    fun `bottomAppBar selects video tab on start and has only one element in the bacStack`() {
        composeAndroidTestRule.run {
            onNodeWithContentDescription(videoListScreen).assertIsSelected()
            onNodeWithContentDescription(shortsScreen).assertIsNotSelected()
            onNodeWithContentDescription(settingsScreen).assertIsNotSelected()
            assertTrue(backStack.size == 1)
        }
    }

    @Test
    fun `bottomAppBar navigates to VideoList on items click`() {
        composeAndroidTestRule.run {
            onNodeWithContentDescription(shortsScreen).performClick()
            onNodeWithContentDescription(videoListScreen).performClick()
            onNodeWithContentDescription(videoListScreen).assertIsSelected()

            verify(backStack).add(any<ScreenType.VideoList>())
            verify(backStack, never()).add(any<ScreenType.SettingsScreen>())
            assertTrue(backStack.size == 2)
        }
    }

    @Test
    fun `bottomAppBar navigates to ShortsScreen on items click`() {
        composeAndroidTestRule.run {
            onNodeWithContentDescription(shortsScreen).performClick()
            onNodeWithContentDescription(shortsScreen).assertIsSelected()

            verify(backStack).add(any<ScreenType.ShortsScreen>())
            verify(backStack, never()).add(any<ScreenType.VideoList>())
            verify(backStack, never()).add(any<ScreenType.SettingsScreen>())
            assertTrue(backStack.size == 2)
        }
    }

    @Test
    fun `bottomAppBar navigates to SettingsScreen on items click`() {
        composeAndroidTestRule.run {
            onNodeWithContentDescription(settingsScreen).performClick()
            onNodeWithContentDescription(settingsScreen).assertIsSelected()

            verify(backStack).add(any<ScreenType.SettingsScreen>())
            verify(backStack, never()).add(any<ScreenType.VideoList>())
            verify(backStack, never()).add(any<ScreenType.ShortsScreen>())
            assertTrue(backStack.size == 2)
        }
    }

    @Test
    fun `bottomAppBar navigation resues existed destinations and moves them at the top of the backStack`() {
        with(composeAndroidTestRule) {
            val indexOfTheFirstElement = 0
            val indexOfTheSecondElement = 1

            onNodeWithContentDescription(shortsScreen).performClick()
            onNodeWithContentDescription(videoListScreen).performClick()

            assertTrue(backStack.size == 2)
            assertTrue(backStack[indexOfTheFirstElement] is ScreenType.ShortsScreen)
            assertTrue(backStack[indexOfTheSecondElement] is ScreenType.VideoList)
        }
    }
}