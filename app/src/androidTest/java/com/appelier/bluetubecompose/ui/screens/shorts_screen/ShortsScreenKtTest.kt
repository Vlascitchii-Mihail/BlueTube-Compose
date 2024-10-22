package com.appelier.bluetubecompose.ui.screens.shorts_screen

import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.testing.TestNavHostController
import androidx.paging.PagingData
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.appelier.bluetubecompose.MainActivity
import com.appelier.bluetubecompose.navigation.ScreenType
import com.appelier.bluetubecompose.screen_shorts.screen.ShortsScreen
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideo
import com.appelier.bluetubecompose.utils.Core
import com.appelier.bluetubecompose.utils.ShortsItemTag
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class ShortsScreenKtTest {

    @get:Rule
    val composeAndroidTestRule = createAndroidComposeRule(MainActivity::class.java)
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    private lateinit var navController: TestNavHostController
    private val videoPage = mutableStateOf(MutableStateFlow(PagingData.from(YoutubeVideo.DEFAULT_VIDEO_LIST)))
    private val emptyVideoPage = mutableStateOf(MutableStateFlow(PagingData.empty<YoutubeVideo>()))

    @Before
    fun injectBeforeTest() {
        hiltRule.inject()
    }

    private fun init_shorts_screen() {
        composeAndroidTestRule.activity.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            NavHost(navController = navController, startDestination = ScreenType.ShortsScreen) {
                composable<ScreenType.ShortsScreen> {
                    ShortsScreen(
                        { videoPage },
                        MutableSharedFlow(3)
                    ) { }
                }
            }

            navController.navigate(ScreenType.ShortsScreen)
        }
    }

    private fun init_empty_shorts_screen() {
        composeAndroidTestRule.activity.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            NavHost(navController = navController, startDestination = ScreenType.ShortsScreen) {
                composable<ScreenType.ShortsScreen> {
                    ShortsScreen(
                        { emptyVideoPage },
                        MutableSharedFlow(3)
                    ) { }
                }
            }
        }
    }

    @Test
    fun shorts_item_is_displayed() {
        init_shorts_screen()

        with(composeAndroidTestRule) {
            onAllNodesWithTag(ShortsItemTag.SHORTS_VIDEO_PLAYER).onFirst().assertIsDisplayed()
            onAllNodesWithTag(Core.CHANNEL_PREVIEW_IMG).onFirst().assertIsDisplayed()
            onAllNodesWithTag(ShortsItemTag.SHORTS_CHANNEL_TITLE).onFirst().assertIsDisplayed()
            onAllNodesWithTag(ShortsItemTag.SHORTS_VIDEO_TITLE).onFirst().assertIsDisplayed()
        }
    }

    @Test
    fun shorts_screen_displays_reload_button_if_list_is_empty() {
        init_empty_shorts_screen()

        with(composeAndroidTestRule) {
            onNodeWithTag(Core.PAGING_ERROR_MSG).assertIsDisplayed()
            onNodeWithTag(Core.CIRCULAR_PROGRESS_INDICATOR).assertIsNotDisplayed()
        }
    }
}
