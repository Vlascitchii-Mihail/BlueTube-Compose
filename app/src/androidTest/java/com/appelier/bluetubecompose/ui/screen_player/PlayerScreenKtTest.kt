package com.appelier.bluetubecompose.ui.screen_player

import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.testing.TestNavHostController
import androidx.navigation.toRoute
import androidx.paging.PagingData
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.appelier.bluetubecompose.MainActivity
import com.appelier.bluetubecompose.core.core_api.VideoApiService
import com.appelier.bluetubecompose.core.core_database.CustomNavTypeSerializer
import com.appelier.bluetubecompose.core.core_database.YouTubeDatabase
import com.appelier.bluetubecompose.navigation.ScreenType
import com.appelier.bluetubecompose.screen_player.PlayerScreen
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideo
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideo.Companion.DEFAULT_VIDEO_LIST
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideoResponse.Companion.DEFAULT_VIDEO_RESPONSE_WITH_CHANNEL_IMG
import com.appelier.bluetubecompose.utils.VideoListScreenTags
import com.appelier.bluetubecompose.utils.VideoPlayerScreenTags
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject
import kotlin.reflect.typeOf

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class PlayerScreenKtTest {

    @get:Rule
    val composeAndroidTestRule = createAndroidComposeRule(MainActivity::class.java)
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var fakeDatabase: YouTubeDatabase
    @Inject
    lateinit var fakeVideoApiService: VideoApiService

    private lateinit var navController: TestNavHostController

    private val firstVideo = DEFAULT_VIDEO_RESPONSE_WITH_CHANNEL_IMG.items.first()
    private val secondVideo = DEFAULT_VIDEO_RESPONSE_WITH_CHANNEL_IMG.items[1]

    @Before
    fun init_player_screen() {
        hiltRule.inject()

        val videoPage = MutableStateFlow(PagingData.from(DEFAULT_VIDEO_LIST))

        composeAndroidTestRule.activity.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())

            NavHost(navController = navController, startDestination = ScreenType.PlayerScreen(firstVideo)) {
                composable<ScreenType.PlayerScreen>(
                    typeMap = mapOf(
                        typeOf<YoutubeVideo>() to CustomNavTypeSerializer(
                            YoutubeVideo::class.java,
                            YoutubeVideo.serializer()
                        )
                    )
                ) { navBackStackEntry ->
                    val video = navBackStackEntry.toRoute<ScreenType.PlayerScreen>().video

                    PlayerScreen(
                        video = video,
                        lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current,
                        relatedVideos = videoPage,
                        navigateToPlayerScreen = {
                            navController.navigate(ScreenType.PlayerScreen(secondVideo)) {
                                launchSingleTop = true
                            }
                        },
                        popBackStack = {}
                    )
                }
            }
        }
    }

    @Test
    fun player_and_RelatedVideos_are_displayed(){
        composeAndroidTestRule.onNodeWithTag(VideoPlayerScreenTags.VIDEO_PLAYER).assertIsDisplayed()
        composeAndroidTestRule.onNodeWithTag(VideoListScreenTags.VIDEO_LIST).assertIsDisplayed()
    }

    @Test
    fun onClick_to_RelatedVideos_opens_new_PlayerScreen() {
        composeAndroidTestRule.onAllNodesWithTag(VideoListScreenTags.VIDEO_PREVIEW_IMG).onFirst().performClick()
    }
}