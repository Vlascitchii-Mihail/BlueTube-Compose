package com.vlascitchii.bluetubecompose.navigation

import android.content.Context
import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vlascitchii.bluetubecompose.MainActivity
import com.vlascitchii.presentation_common.ui.theme.BlueTubeComposeTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.any
import org.mockito.kotlin.spy
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import com.vlascitchii.common_ui.R as RCommon
import com.vlascitchii.player_screen.R as RPlayer
import com.vlascitchii.video_list_screen.R as RVideoList

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class BlueTubeNavigationTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeAndroidTestRule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity> =
        createAndroidComposeRule(MainActivity::class.java)

    private val context = ApplicationProvider.getApplicationContext<Context>()

    private val videoListScreenDescription: String = context.getString(RVideoList.string.video_list_screen_description)
    private val playerScreenDescription: String = context.getString(RPlayer.string.player_screen_description)
    private val videoItemDescription: String = context.getString(RCommon.string.video_compact_preview_description)
    private lateinit var backStack: NavBackStack<NavKey>

    @Before
    fun init() {
        hiltRule.inject()

        with(composeAndroidTestRule.activity) {
            backStack = spy(NavBackStack(ScreenType.VideoList()))

            setContent {
                BlueTubeComposeTheme {
                    BlueTubeNavigation(backStack = backStack)
                }
            }
        }
    }

    @Test
    fun `At start the Navigation has only the VideoListScreen in the BackStack`() {
        composeAndroidTestRule.run {
            val oneElementInTheStack = 1
            onNodeWithContentDescription(videoListScreenDescription).assertIsDisplayed()
            assertTrue(backStack.size == oneElementInTheStack)
        }
    }

    @Test
    fun `Click on any video thumbnail in the VideoList Screen navigates to the PlayerScreen`() {
        composeAndroidTestRule.run {
            onNodeWithContentDescription(videoListScreenDescription).assertIsDisplayed()
            onAllNodesWithContentDescription(videoItemDescription).onFirst().performClick()
            onNodeWithContentDescription(playerScreenDescription).assertIsDisplayed()
        }

    }

    @Test
    fun `Click on any video thumbnail in the PlayerScreen related videos navigates to a new PlayerScreen`() {
        composeAndroidTestRule.run {
            onAllNodesWithContentDescription(videoItemDescription).onFirst().performClick()
            onNodeWithContentDescription(playerScreenDescription).assertIsDisplayed()
            verify(backStack, times(1)).add(any<ScreenType.PlayerScreen>())

            onAllNodesWithContentDescription(videoItemDescription).onFirst().performClick()
            onNodeWithContentDescription(playerScreenDescription).assertIsDisplayed()
            verify(backStack, times(2)).add(any<ScreenType.PlayerScreen>())
        }
    }

    @Test
    fun `Back gesture navigates back from PlayerScreen to the VideoListScreen`() {
        val singleElementInBackStack = 1
        composeAndroidTestRule.run {
            onAllNodesWithContentDescription(videoItemDescription).onFirst().performClick()
            onNodeWithContentDescription(playerScreenDescription).assertIsDisplayed()

            Espresso.pressBack()
            onNodeWithContentDescription(videoListScreenDescription).assertIsDisplayed()
            assertTrue(backStack.size == singleElementInBackStack)
        }
    }
}
