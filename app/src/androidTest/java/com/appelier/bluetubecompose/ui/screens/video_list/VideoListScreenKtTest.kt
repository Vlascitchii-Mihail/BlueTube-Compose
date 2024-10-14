package com.appelier.bluetubecompose.ui.screens.video_list

import androidx.activity.compose.setContent
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
import com.appelier.bluetubecompose.core.core_database.CustomNavTypeSerializer
import com.appelier.bluetubecompose.navigation.ScreenType
import com.appelier.bluetubecompose.screen_player.OrientationState
import com.appelier.bluetubecompose.screen_player.PlayerScreen
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideo
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideo.Companion.DEFAULT_VIDEO_LIST
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideoResponse
import com.appelier.bluetubecompose.screen_video_list.screen.VideoListScreen
import com.appelier.bluetubecompose.search_video.SearchState
import com.appelier.bluetubecompose.utils.Core.CHANNEL_PREVIEW_IMG
import com.appelier.bluetubecompose.utils.VideoListScreenTags
import com.appelier.bluetubecompose.utils.VideoListScreenTags.VIDEO_LIST_ERROR
import com.appelier.bluetubecompose.utils.VideoPlayerScreenTags
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.reflect.typeOf

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class VideoListScreenKtTest {

    @get:Rule
    val composeAndroidTestRule = createAndroidComposeRule(MainActivity::class.java)
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    private val testSearchState: MutableStateFlow<SearchState> = MutableStateFlow(SearchState.CLOSED)
    private val testSearchTextState: MutableStateFlow<String> = MutableStateFlow("")
    private lateinit var navController: TestNavHostController
    private val video = YoutubeVideoResponse.DEFAULT_VIDEO_RESPONSE_WITH_CHANNEL_IMG.items.first()
    private val videoPage = MutableStateFlow(PagingData.from(DEFAULT_VIDEO_LIST))
    private val emptyVideoPage = MutableStateFlow(PagingData.empty<YoutubeVideo>())

    @Before
    fun init_video_list_screen() {
        hiltRule.inject()
    }

    private fun launchNavigation(videoList: MutableStateFlow<PagingData<YoutubeVideo>>) {
        composeAndroidTestRule.activity.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            NavHost(navController = navController, startDestination = ScreenType.VideoList) {
                composable<ScreenType.VideoList> {
                    VideoListScreen(
                        navigateToPlayerScreen = {
                            navController.navigate(ScreenType.PlayerScreen(video))
                        },
                        searchViewState = testSearchState,
                        searchTextState = testSearchTextState,
                        videos = { mutableStateOf(videoList) },
                        updateSearchTextState = { searchText -> testSearchTextState.value = searchText },
                        updateSearchState = { searchState -> testSearchState.value = searchState },
                        setSearchVideosFlow = { videoList }
                    )
                }
                composable<ScreenType.PlayerScreen>(
                    typeMap = mapOf(
                        typeOf<YoutubeVideo>() to CustomNavTypeSerializer(
                            YoutubeVideo::class.java,
                            YoutubeVideo.serializer()
                        )
                    )
                ) {
                    val initialPlaybackPosition = 0F
                    PlayerScreen(
                        video = video,
                        relatedVideos = { mutableStateOf(MutableStateFlow(PagingData.from(DEFAULT_VIDEO_LIST))) },
                        isVideoPlaysFlow = MutableStateFlow(true),
                        {},
                        navigateToPlayerScreen = {},
                        popBackStack = {},
                        updatePlaybackPosition = {},
                        getPlaybackPosition = { initialPlaybackPosition },
                        MutableStateFlow(OrientationState.PORTRAIT),
                        {},
                        MutableStateFlow(false),
                        {}
                    )
                }
            }
        }
    }

    @Test
    fun app_shows_appbar_and_search_app_bar() {
        launchNavigation(videoPage)
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
        launchNavigation(videoPage)

        with(composeAndroidTestRule) {
            waitForIdle()
            onNodeWithTag(VideoListScreenTags.VIDEO_LIST).assertIsDisplayed()
            onAllNodesWithTag(VideoListScreenTags.VIDEO_PREVIEW_IMG).onFirst().assertIsDisplayed()
            onAllNodesWithTag(CHANNEL_PREVIEW_IMG).onFirst().isDisplayed()
            onAllNodesWithTag(VideoListScreenTags.VIDEO_TITLE).onFirst().assertIsDisplayed()
            onAllNodes(hasContentDescription(VideoListScreenTags.VIDEO_DURATION)).onFirst().assertIsDisplayed()
            onAllNodesWithTag(VideoListScreenTags.VIDEO_STATISTICS).onFirst().assertIsDisplayed()
        }
    }

    @Test
    fun onClick_to_video_preview_navigates_to_PlayerScreen() {
        launchNavigation(videoPage)
        with(composeAndroidTestRule) {
            onAllNodesWithTag(VideoListScreenTags.VIDEO_PREVIEW_IMG).onFirst().performClick()
            onNodeWithTag(VideoPlayerScreenTags.VIDEO_PLAYER).assertIsDisplayed()
            onNodeWithTag(VideoListScreenTags.VIDEO_LIST).assertIsDisplayed()
        }
    }

    @Test
    fun emptyVideoListShowsPaginationErrorItemComposable() {
        launchNavigation(emptyVideoPage)
        composeAndroidTestRule.onNodeWithTag(VIDEO_LIST_ERROR).assertIsDisplayed()
    }
}
