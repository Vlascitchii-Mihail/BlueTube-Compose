package com.vlascitchii.presentation_video_list.video_list

import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.testing.TestNavHostController
import androidx.paging.PagingData
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.appelier.bluetubecompose.MainActivity
import com.appelier.bluetubecompose.R
import com.vlascitchii.presentation_common.ui.video_list.YouTubeVideoList
import com.appelier.bluetubecompose.navigation.CustomNavTypeSerializer
import com.appelier.bluetubecompose.navigation.ScreenType
import com.vlascitchii.data_local.rule.DispatcherTestRule
import com.vlascitchii.presentation_player.screen_player.OrientationState
import com.vlascitchii.presentation_player.screen_player.PlayerScreen
import com.vlascitchii.data_local.enetity.video_list.videos.YoutubeVideoEntity
import com.vlascitchii.data_local.enetity.video_list.videos.YoutubeVideoEntity.Companion.DEFAULT_VIDEO_LIST
import com.vlascitchii.data_remote.enetity_api_model.video_list_api_model.videos_api_model.YoutubeVideoResponseApiModel
import com.vlascitchii.data_remote.enetity_api_model.video_list_api_model.videos_api_model.YoutubeVideoResponseApiModel.Companion.RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG
import com.vlascitchii.presentation_common.utils.Core.CHANNEL_PREVIEW_IMG
import com.vlascitchii.presentation_common.utils.VideoListScreenTags
import com.vlascitchii.presentation_common.utils.VideoPlayerScreenTags
//import dagger.hilt.android.testing.HiltAndroidRule
//import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.reflect.typeOf

@RunWith(AndroidJUnit4::class)
//@HiltAndroidTest
class VideoListScreenKtTest {

    @get:Rule
    val composeAndroidTestRule = createAndroidComposeRule(MainActivity::class.java)
//    @get:Rule
//    val hiltRule = HiltAndroidRule(this)
    @get:Rule
    val dispatcherTestRule = com.vlascitchii.data_local.rule.DispatcherTestRule()

    private val testSearchState: MutableStateFlow<com.vlascitchii.presentation_video_list.search_video.SearchState> = MutableStateFlow(
        com.vlascitchii.presentation_video_list.search_video.SearchState.CLOSED)
    private val testSearchTextState: MutableStateFlow<String> = MutableStateFlow("")
    private lateinit var navController: TestNavHostController
    private var video = RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG.items.first()
    private val videoPage = MutableStateFlow(PagingData.from(DEFAULT_VIDEO_LIST))
    private var videoState: State<StateFlow<PagingData<YoutubeVideoEntity>>> = mutableStateOf(videoPage)


    @Before
    fun init_video_list_screen() {
//        hiltRule.inject()
        launchNavigation()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    private fun launchNavigation() {
        composeAndroidTestRule.activity.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())

            NavHost(navController = navController, startDestination = com.appelier.bluetubecompose.navigation.ScreenType.VideoList) {
                composable<com.appelier.bluetubecompose.navigation.ScreenType.VideoList> {
                    com.vlascitchii.presentation_video_list.screen_video_list.screen.VideoListScreen(
                        connectivityStatus = com.appelier.bluetubecompose.network_observer.NetworkConnectivityObserver(
                            context = composeAndroidTestRule.activity.applicationContext
                        ).observe(),
                        listScreenAppBar = {
                            com.vlascitchii.presentation_video_list.video_list_screen.ListScreenAppBar(
                                searchViewState = testSearchState,
                                searchTextState = testSearchTextState,
                                scrollAppBarBehaviour = TopAppBarDefaults.enterAlwaysScrollBehavior(
                                    rememberTopAppBarState()
                                ),
                                onTextChange = { searchInput: String ->
                                    testSearchTextState.value = searchInput
                                },
                                onSearchClicked = {
                                    videoState = mutableStateOf(
                                        MutableStateFlow(
                                            PagingData.from(
                                                YoutubeVideoResponseApiModel.VIDEO_LIST_WITH_CHANNEL_IMG_URL
                                            )
                                        )
                                    )
                                },
                                updateSearchState = { newSearchState: com.vlascitchii.presentation_video_list.search_video.SearchState ->
                                    testSearchState.value = newSearchState
                                }
                            )
                        },
                        videoList = {
                            YouTubeVideoList(
                                getVideoState = { videoState },
                                navigateToPlayerScreen = {
                                    navController.navigate(
                                        com.appelier.bluetubecompose.navigation.ScreenType.PlayerScreen(
                                            video
                                        )
                                    )
                                }
                            )
                        }
                    )
                }
                composable<com.appelier.bluetubecompose.navigation.ScreenType.PlayerScreen>(
                    typeMap = mapOf(
                        typeOf<YoutubeVideoEntity>() to com.appelier.bluetubecompose.navigation.CustomNavTypeSerializer(
                            YoutubeVideoEntity::class.java,
                            YoutubeVideoEntity.serializer()
                        )
                    )
                ) {
                    val initialPlaybackPosition = 0F
                    com.vlascitchii.presentation_player.screen_player.PlayerScreen(
                        video = video,
                        relatedVideos = MutableStateFlow(
                            PagingData.from(
                                RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG.items
                            )
                        ),
                        getRelatedVideos = { query: String -> },
                        isVideoPlaysFlow = MutableStateFlow(true),
                        updateVideoIsPlayState = { isPlaying: Boolean -> },
                        navigateToPlayerScreen = {},
                        popBackStack = {},
                        updatePlaybackPosition = {},
                        getPlaybackPosition = { initialPlaybackPosition },
                        playerOrientationState = MutableStateFlow(com.vlascitchii.presentation_player.screen_player.OrientationState.PORTRAIT),
                        updatePlayerOrientationState = { newPlayerOrientationState: com.vlascitchii.presentation_player.screen_player.OrientationState -> },
                        fullscreenWidgetIsClicked = MutableStateFlow(false),
                        setFullscreenWidgetIsClicked = { isClicked: Boolean -> },
                        connectivityStatus = flowOf(com.appelier.bluetubecompose.network_observer.ConnectivityStatus.Available)
                    )
                }
            }
        }
    }

    @Test
    fun app_shows_appbar_and_search_app_bar() {
        with(composeAndroidTestRule) {
            val appName = activity.getString(R.string.appbar_title)
            val searchPlaceholder = activity.getString(R.string.search_placeholder)

            onNodeWithText(appName).assertIsDisplayed()
            onNodeWithContentDescription(activity.getString(R.string.appbar_search_icon_descr)).performClick()
            onNodeWithText(appName).assertIsNotDisplayed()

            onNodeWithText(searchPlaceholder).assertIsDisplayed()
            onNodeWithContentDescription(activity.getString(R.string.appbar_close_icon_descr)).assertIsDisplayed()
            onNodeWithText(searchPlaceholder).performClick()
            onNodeWithText(searchPlaceholder).performTextInput(appName)

            onNodeWithContentDescription(activity.getString(R.string.appbar_close_icon_descr)).performClick()
            onNodeWithText(searchPlaceholder).assertIsDisplayed()
            onNodeWithContentDescription(activity.getString(R.string.appbar_close_icon_descr)).performClick()
            onNodeWithText(appName).assertIsDisplayed()
        }
    }

    @Test
    fun app_shows_video_list() {
        with(composeAndroidTestRule) {
            waitForIdle()
            onNodeWithTag(com.vlascitchii.presentation_common.utils.VideoListScreenTags.VIDEO_LIST).assertIsDisplayed()
            onAllNodesWithTag(com.vlascitchii.presentation_common.utils.VideoListScreenTags.VIDEO_PREVIEW_IMG).onFirst().assertIsDisplayed()
            onAllNodesWithTag(CHANNEL_PREVIEW_IMG).onFirst().isDisplayed()
            onAllNodesWithTag(com.vlascitchii.presentation_common.utils.VideoListScreenTags.VIDEO_TITLE).onFirst().assertIsDisplayed()
            onAllNodes(hasContentDescription(com.vlascitchii.presentation_common.utils.VideoListScreenTags.VIDEO_DURATION)).onFirst().assertIsDisplayed()
            onAllNodesWithTag(com.vlascitchii.presentation_common.utils.VideoListScreenTags.VIDEO_STATISTICS).onFirst().assertIsDisplayed()
        }
    }

    @Test
    fun onClick_to_video_preview_navigates_to_PlayerScreen() {
        with(composeAndroidTestRule) {
            onAllNodesWithTag(com.vlascitchii.presentation_common.utils.VideoListScreenTags.VIDEO_PREVIEW_IMG).onFirst().performClick()
            onNodeWithTag(com.vlascitchii.presentation_common.utils.VideoPlayerScreenTags.VIDEO_PLAYER).assertIsDisplayed()
            onNodeWithTag(com.vlascitchii.presentation_common.utils.VideoListScreenTags.VIDEO_LIST).assertIsDisplayed()
        }
    }
}
