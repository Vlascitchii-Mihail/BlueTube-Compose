package com.vlascitchii.bluetubecompose.navigation

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vlascitchii.bluetubecompose.MainActivity
import com.vlascitchii.presentation_common.ui.theme.BlueTubeComposeTheme
import com.vlascitchii.presentation_video_list.screen.VideoListViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.spy
import com.vlascitchii.presentation_common.R as RCommon
import com.vlascitchii.presentation_player.R as RPlayer
import com.vlascitchii.presentation_shorts.R as RShorts
import com.vlascitchii.presentation_video_list.R as RVideoList
import com.vlascitchii.presenttion_settings.R as RSettings

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class BlueTubeNavigationTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeAndroidTestRule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity> =
        createAndroidComposeRule(MainActivity::class.java)

    private lateinit var videoListScreenDescription: String
    private lateinit var playerScreenDescription: String
    private lateinit var shortsScreenDescription: String
    private lateinit var settingsScreenDescription: String
    private lateinit var videoItemDescription: String
    private lateinit var backStack: NavBackStack<NavKey>

    @Before
    fun init() {
        with(composeAndroidTestRule.activity) {
            videoListScreenDescription = getString(RVideoList.string.video_list_screen_description)
            playerScreenDescription = getString(RPlayer.string.player_screen_description)
            shortsScreenDescription = getString(RShorts.string.shorts_screen_description)
            settingsScreenDescription = getString(RSettings.string.settings_screen_description)
            videoItemDescription = getString(RCommon.string.video_compact_preview_description)
            backStack = spy(NavBackStack(ScreenType.VideoList()))

            setContent {
                val videoListViewModel: VideoListViewModel = hiltViewModel()

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

    // TODO: Refactor ViewModel parameters to Dependency Inversion to be able to create stest DI modules for injecting fake ViewModel objects into the Navigation
    @Test
    fun `Click on any video thumbnail in the VideoList Screen navigates to the PlayerScreen`() {

    }

    // TODO: Refactor ViewModel parameters to Dependency Inversion to be able to create stest DI modules for injecting fake ViewModel objects into the Navigation
    @Test
    fun `Click on any video thumbnail in the PlayerScreen related videos navigates to a new PlayerScreen`() {

    }

    // TODO: Refactor ViewModel parameters to Dependency Inversion to be able to create stest DI modules for injecting fake ViewModel objects into the Navigation
    @Test
    fun `Click on a related video navigates to a new PlayerScreen and Back gesture navigates to the previous PlayerScreen`() {

    }

    // TODO: Refactor ViewModel parameters to Dependency Inversion to be able to create stest DI modules for injecting fake ViewModel objects into the Navigation
    @Test
    fun `Back gesture navigates back from PlayerScreen to the VideoListScreen`() {

    }
}
