package com.appelier.bluetubecompose.ui.screen_player

import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
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
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.device.DeviceInteraction.Companion.setScreenOrientation
import androidx.test.espresso.device.EspressoDevice.Companion.onDevice
import androidx.test.espresso.device.action.ScreenOrientation
import androidx.test.espresso.device.rules.ScreenOrientationRule
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.appelier.bluetubecompose.MainActivity
import com.appelier.bluetubecompose.R
import com.appelier.bluetubecompose.core.core_api.VideoApiService
import com.appelier.bluetubecompose.core.core_database.CustomNavTypeSerializer
import com.appelier.bluetubecompose.core.core_database.YouTubeDatabase
import com.appelier.bluetubecompose.navigation.ScreenType
import com.appelier.bluetubecompose.screen_player.PlayerScreen
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideo
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
    @get:Rule
    val screenOrientation = ScreenOrientationRule(ScreenOrientation.PORTRAIT)

    @Inject
    lateinit var fakeDatabase: YouTubeDatabase
    @Inject
    lateinit var fakeVideoApiService: VideoApiService

    private lateinit var navController: TestNavHostController
    private val videoPage = mutableStateOf(MutableStateFlow(PagingData.from(YoutubeVideo.DEFAULT_VIDEO_LIST)))
    private val firstVideo = DEFAULT_VIDEO_RESPONSE_WITH_CHANNEL_IMG.items.first()
    private val secondVideo = DEFAULT_VIDEO_RESPONSE_WITH_CHANNEL_IMG.items[1]
    private var videoId: String = ""

    @Before
    fun init_player_screen() {
        hiltRule.inject()
        setNavigationScreenIntoActivity()
    }

    private fun setNavigationScreenIntoActivity() {
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
                    videoId = video.id

                    PlayerScreen(
                        video = video,
                        relatedVideos = videoPage,
                        mutableStateOf(true),
                        navigateToPlayerScreen = {
                            navController.navigate(ScreenType.PlayerScreen(secondVideo)) {
                                launchSingleTop = true
                            }
                        },
                        popBackStack = {},
                        updatePlaybackPosition = {},
                        getPlaybackPosition = { 0F }
                    )
                }
            }
        }
    }

    @Test
    fun player_videoDescription_and_RelatedVideos_are_displayed(){
        with(composeAndroidTestRule) {
           onNodeWithTag(VideoPlayerScreenTags.VIDEO_PLAYER).assertIsDisplayed()
           onNodeWithTag(VideoPlayerScreenTags.VIDEO_DESCRIPTION).assertIsDisplayed()
           onNodeWithTag(VideoListScreenTags.VIDEO_LIST).assertIsDisplayed()
        }
    }

    @Test
    fun onClick_to_RelatedVideos_opens_new_PlayerScreen() {
        composeAndroidTestRule.onAllNodesWithTag(VideoListScreenTags.VIDEO_PREVIEW_IMG).onFirst().performClick()
        assertNotEquals("", videoId)
        assertEquals(firstVideo.id, videoId)

        composeAndroidTestRule.onAllNodesWithTag(VideoListScreenTags.VIDEO_PREVIEW_IMG).onFirst().performClick()
        assertNotEquals(firstVideo.id, videoId)
        assertEquals(secondVideo.id, videoId)
    }

    @Test
    fun check_onLandscape_state_only_player_isVisible() {
        with(composeAndroidTestRule) {
            changeOrientationTo(ScreenOrientation.LANDSCAPE)
            setNavigationScreenIntoActivity()
            onView(withId(R.id.ll_player_container)).check(matches(isDisplayed()))
            onNodeWithTag(VideoPlayerScreenTags.VIDEO_PLAYER).assertIsDisplayed()
            onNodeWithTag(VideoPlayerScreenTags.VIDEO_DESCRIPTION).assertIsNotDisplayed()
            onNodeWithTag(VideoListScreenTags.VIDEO_LIST).assertIsNotDisplayed()

            changeOrientationTo(ScreenOrientation.PORTRAIT)
            setNavigationScreenIntoActivity()
            onView(withId(R.id.ll_player_container)).check(matches(isDisplayed()))
            onNodeWithTag(VideoPlayerScreenTags.VIDEO_PLAYER).assertIsDisplayed()
            onNodeWithTag(VideoPlayerScreenTags.VIDEO_DESCRIPTION).assertIsDisplayed()
            onNodeWithTag(VideoListScreenTags.VIDEO_LIST).assertIsDisplayed()
        }
    }

    private fun changeOrientationTo(newOrientation: ScreenOrientation) {
        onDevice().setScreenOrientation(newOrientation)
    }
}