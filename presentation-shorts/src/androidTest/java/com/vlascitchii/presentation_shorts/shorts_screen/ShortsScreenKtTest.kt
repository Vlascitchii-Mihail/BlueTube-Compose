package com.vlascitchii.presentation_shorts.shorts_screen

import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onFirst
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.testing.TestNavHostController
import androidx.paging.PagingData
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.appelier.bluetubecompose.MainActivity
import com.appelier.bluetubecompose.navigation.ScreenType
import com.vlascitchii.presentation_shorts.screen_shorts.screen.ShortsScreen
import com.vlascitchii.data_local.enetity.video_list.videos.YoutubeVideoEntity
//import dagger.hilt.android.testing.HiltAndroidRule
//import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
//@HiltAndroidTest
class ShortsScreenKtTest {

    @get:Rule
    val composeAndroidTestRule = createAndroidComposeRule(MainActivity::class.java)
//    @get:Rule
//    val hiltRule = HiltAndroidRule(this)

    private lateinit var navController: TestNavHostController
    private val videoPage = MutableStateFlow(PagingData.from(YoutubeVideoEntity.DEFAULT_VIDEO_LIST))

    @Before
    fun injectBeforeTest() {
//        hiltRule.inject()
        init_shorts_screen()
    }

    private fun init_shorts_screen() {
        composeAndroidTestRule.activity.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            NavHost(navController = navController, startDestination = com.appelier.bluetubecompose.navigation.ScreenType.ShortsScreen) {
                composable<com.appelier.bluetubecompose.navigation.ScreenType.ShortsScreen> {
                    com.vlascitchii.presentation_shorts.screen_shorts.screen.ShortsScreen(
                        shortsStateFlow = videoPage,
                        videoQueue = MutableSharedFlow(3),
                        listenToVideoQueue = {},
                        networkConnectivityStatus = flowOf(com.appelier.bluetubecompose.network_observer.ConnectivityStatus.Available),
                    )
                }
            }

            navController.navigate(com.appelier.bluetubecompose.navigation.ScreenType.ShortsScreen)
        }
    }

    @Test
    fun shorts_item_is_displayed() {
        with(composeAndroidTestRule) {
            onAllNodesWithTag(com.vlascitchii.presentation_common.utils.ShortsItemTag.SHORTS_VIDEO_PLAYER).onFirst().assertIsDisplayed()
            onAllNodesWithTag(com.vlascitchii.presentation_common.utils.Core.CHANNEL_PREVIEW_IMG).onFirst().assertIsDisplayed()
            onAllNodesWithTag(com.vlascitchii.presentation_common.utils.ShortsItemTag.SHORTS_CHANNEL_TITLE).onFirst().assertIsDisplayed()
            onAllNodesWithTag(com.vlascitchii.presentation_common.utils.ShortsItemTag.SHORTS_VIDEO_TITLE).onFirst().assertIsDisplayed()
        }
    }
}
