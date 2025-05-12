package com.vlascitchii.presentation_player

import androidx.activity.compose.setContent
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
import com.appelier.bluetubecompose.network_observer.NetworkConnectivityObserver
import com.appelier.bluetubecompose.navigation.CustomNavTypeSerializer
import com.appelier.bluetubecompose.navigation.ScreenType
import com.vlascitchii.presentation_player.screen_player.screen.PlayerScreen
import com.vlascitchii.data_local.enetity.video_list.videos.YoutubeVideoEntity
import com.vlascitchii.data_remote.enetity_api_model.video_list_api_model.videos_api_model.YoutubeVideoResponseApiModel.Companion.RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG
import com.appelier.bluetubecompose.network_observer.ConnectivityStatus
import com.vlascitchii.presentation_video_list.screen_video_list.repository.VideoListRepository
import com.vlascitchii.presentation_common.utils.VideoListScreenTags
import com.vlascitchii.presentation_common.utils.VideoPlayerScreenTags
import com.vlascitchii.presentation_player.screen_player.screen.VideoPlayerViewModel
//import dagger.hilt.android.testing.HiltAndroidRule
//import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.mockito.kotlin.spy
import kotlin.reflect.typeOf

@RunWith(AndroidJUnit4::class)
//@HiltAndroidTest
class PlayerScreenKtTest {

    @get:Rule
    val composeAndroidTestRule = createAndroidComposeRule(MainActivity::class.java)

//    @get:Rule
//    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val screenOrientation = ScreenOrientationRule(ScreenOrientation.PORTRAIT)

    private lateinit var navController: TestNavHostController
    private val firstVideo = RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG.items.first()
    private val secondVideo = RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG.items[1]
    private var videoId: String = ""
    private val repository: com.vlascitchii.presentation_video_list.screen_video_list.repository.VideoListRepository = mock()
    private lateinit var viewModel: VideoPlayerViewModel

    private lateinit var videoThumbnailDescription: String

    @Before
    fun init_player_screen() {
//        hiltRule.inject()
        videoThumbnailDescription =
            composeAndroidTestRule.activity.getString(R.string.video_thumbnail_description)
        setNavigationScreenIntoActivity()
    }

    private fun setNavigationScreenIntoActivity() {
        composeAndroidTestRule.activity.setContent {

            viewModel = spy(
                VideoPlayerViewModel(
                    repository,
                    com.appelier.bluetubecompose.network_observer.NetworkConnectivityObserver(
                        composeAndroidTestRule.activity.applicationContext
                    )
                )
            )

            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())

            NavHost(
                navController = navController,
                startDestination = com.appelier.bluetubecompose.navigation.ScreenType.PlayerScreen(firstVideo)
            ) {
                composable<com.appelier.bluetubecompose.navigation.ScreenType.PlayerScreen>(
                    typeMap = mapOf(
                        typeOf<YoutubeVideoEntity>() to com.appelier.bluetubecompose.navigation.CustomNavTypeSerializer(
                            YoutubeVideoEntity::class.java,
                            YoutubeVideoEntity.serializer()
                        )
                    )
                ) { navBackStackEntry ->
                    val video = navBackStackEntry.toRoute<com.appelier.bluetubecompose.navigation.ScreenType.PlayerScreen>().video
                    videoId = video.id

                    PlayerScreen(
                        video = video,
                        relatedVideos = MutableStateFlow(
                            PagingData.from(
                                RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG.items
                            )
                        ),
                        getRelatedVideos = { query: String -> },
                        isVideoPlaysFlow = MutableStateFlow(true),
                        updateVideoIsPlayState = { isPlaying: Boolean -> },
                        navigateToPlayerScreen = {
                            navController.navigate(
                                com.appelier.bluetubecompose.navigation.ScreenType.PlayerScreen(
                                    secondVideo
                                )
                            ) {
                                launchSingleTop = true
                            }
                        },
                        popBackStack = {},
                        updatePlaybackPosition = {},
                        getPlaybackPosition = { 0F },
                        playerOrientationState = viewModel.playerOrientationState,
                        updatePlayerOrientationState = { newPlayerOrientationState ->
                            viewModel.updatePlayerOrientationState(
                                newPlayerOrientationState
                            )
                        },
                        fullscreenWidgetIsClicked = viewModel.fullscreenWidgetIsClicked,
                        setFullscreenWidgetIsClicked = { isClicked ->
                            viewModel.setFullscreenWidgetIsClicked(
                                isClicked
                            )
                        },
                        connectivityStatus = flowOf(com.appelier.bluetubecompose.network_observer.ConnectivityStatus.Available)
                    )
                }
            }
        }
    }

    @Test
    fun player_videoDescription_and_RelatedVideos_are_displayed() {
        with(composeAndroidTestRule) {
            onNodeWithTag(com.vlascitchii.presentation_common.utils.VideoPlayerScreenTags.VIDEO_PLAYER).assertIsDisplayed()
            onNodeWithTag(com.vlascitchii.presentation_common.utils.VideoPlayerScreenTags.VIDEO_DESCRIPTION).assertIsDisplayed()
            onNodeWithTag(com.vlascitchii.presentation_common.utils.VideoListScreenTags.VIDEO_LIST).assertIsDisplayed()
        }
    }

    @Test
    fun onClick_to_RelatedVideos_opens_new_PlayerScreen() {
        composeAndroidTestRule.onAllNodesWithTag(com.vlascitchii.presentation_common.utils.VideoListScreenTags.VIDEO_PREVIEW_IMG).onFirst()
            .performClick()
        assertNotEquals("", videoId)
        assertEquals(firstVideo.id, videoId)

        composeAndroidTestRule.onAllNodesWithTag(com.vlascitchii.presentation_common.utils.VideoListScreenTags.VIDEO_PREVIEW_IMG).onFirst()
            .performClick()
        assertNotEquals(firstVideo.id, videoId)
        assertEquals(secondVideo.id, videoId)
    }

    @Test
    fun check_onLandscape_state_only_player_isVisible() {
        with(composeAndroidTestRule) {
            changeOrientationTo(ScreenOrientation.LANDSCAPE)
            setNavigationScreenIntoActivity()
            onView(withId(R.id.ll_player_container)).check(matches(isDisplayed()))
            onNodeWithTag(com.vlascitchii.presentation_common.utils.VideoPlayerScreenTags.VIDEO_PLAYER).assertIsDisplayed()
            onNodeWithTag(com.vlascitchii.presentation_common.utils.VideoPlayerScreenTags.VIDEO_DESCRIPTION).assertIsNotDisplayed()
            onNodeWithTag(com.vlascitchii.presentation_common.utils.VideoListScreenTags.VIDEO_LIST).assertIsNotDisplayed()

            changeOrientationTo(ScreenOrientation.PORTRAIT)
            setNavigationScreenIntoActivity()
            onView(withId(R.id.ll_player_container)).check(matches(isDisplayed()))
            onNodeWithTag(com.vlascitchii.presentation_common.utils.VideoPlayerScreenTags.VIDEO_PLAYER).assertIsDisplayed()
            onNodeWithTag(com.vlascitchii.presentation_common.utils.VideoPlayerScreenTags.VIDEO_DESCRIPTION).assertIsDisplayed()
            onNodeWithTag(com.vlascitchii.presentation_common.utils.VideoListScreenTags.VIDEO_LIST).assertIsDisplayed()
        }
    }

    private fun changeOrientationTo(newOrientation: ScreenOrientation) {
        onDevice().setScreenOrientation(newOrientation)
    }
}
