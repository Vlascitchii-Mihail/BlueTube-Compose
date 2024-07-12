package com.appelier.bluetubecompose.ui

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.lifecycle.viewModelScope
import androidx.navigation.compose.rememberNavController
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.appelier.bluetubecompose.R
import com.appelier.bluetubecompose.core.core_api.VideoApiService
import com.appelier.bluetubecompose.core.core_database.YouTubeDatabase
import com.appelier.bluetubecompose.core.core_paging.YoutubeVideoSource
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideo
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideoResponse.Companion.DEFAULT_VIDEO_RESPONSE_WITH_CHANNEL_IMG
import com.appelier.bluetubecompose.screen_video_list.screen.VideoListScreen
import com.appelier.bluetubecompose.screen_video_list.screen.VideoListViewModel
import com.appelier.bluetubecompose.search_video.SearchState
import com.appelier.bluetubecompose.utils.VideoListScreenTags
import com.appelier.bluetubecompose.utils.VideoType
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class VideoListScreenTest {

    @get:Rule(order  = 0)
    val composeTestRule = createComposeRule()

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var fakeDatabase: YouTubeDatabase
    @Inject
    lateinit var fakeVideoApiService: VideoApiService
    private val viewModel: VideoListViewModel = mock()

    private val searchState: MutableState<SearchState> = mutableStateOf(SearchState.CLOSED)

    private val searchTextState: MutableState<String> = mutableStateOf("")


    @Before
    fun init_video_list_screen() {
        hiltRule.inject()

        composeTestRule.setContent {

            val videoPage: StateFlow<PagingData<YoutubeVideo>> =
                Pager(PagingConfig(pageSize = 5, prefetchDistance = 0)) {
                    YoutubeVideoSource(
                        fakeVideoApiService,
                        viewModel.viewModelScope,
                        VideoType.Videos,
                        fakeDatabase.youTubeVideoDao
                    )
                }.flow.stateIn(
                    viewModel.viewModelScope,
                    SharingStarted.Lazily,
                    PagingData.from(DEFAULT_VIDEO_RESPONSE_WITH_CHANNEL_IMG.items)
                )


            VideoListScreen(
                rememberNavController(),
                searchState.value,
                searchTextState.value,
                videoPage,
                updateSearchTextState = { searchText -> searchTextState.value = searchText },
                updateSearchState = { searchState -> this.searchState.value = searchState },
                getSearchVideosFlow = { searchText ->
                    viewModel.getSearchVideosFlow("")
                    videoPage
                }
            )
        }
    }

    @Test
    fun app_shows_appbar_and_search_app_bar() {
        val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
        val appName = context.getString(R.string.appbar_title)
        val searchPlaceholder = context.getString(R.string.search_placeholder)

        with(composeTestRule) {
            onNodeWithText(appName).assertIsDisplayed()
            onNodeWithTag(VideoListScreenTags.ICON_OPEN_SEARCH).performClick()
            onNodeWithText(appName).assertIsNotDisplayed()

            onNodeWithText(searchPlaceholder).assertIsDisplayed()
            onNodeWithTag(VideoListScreenTags.ICON_CLOSE_SEARCH).assertIsDisplayed()
            onNodeWithText(searchPlaceholder).performClick()
            onNodeWithText(searchPlaceholder).performTextInput(appName)

            onNodeWithTag(VideoListScreenTags.ICON_CLOSE_SEARCH).performClick()
            onNodeWithText(searchPlaceholder).assertIsDisplayed()
            onNodeWithTag(VideoListScreenTags.ICON_CLOSE_SEARCH).performClick()
            onNodeWithText(appName).assertIsDisplayed()
        }
    }

    @Test
    fun app_shows_video_list() {
        composeTestRule.onNodeWithTag(VideoListScreenTags.VIDEO_LIST).assertIsDisplayed()
        composeTestRule.onAllNodesWithTag(VideoListScreenTags.VIDEO_PREVIEW_IMG).onFirst().assertIsDisplayed()
        composeTestRule.onAllNodesWithTag(VideoListScreenTags.CHANNEL_PREVIEW_IMG).onFirst().assertIsDisplayed()
        composeTestRule.onAllNodesWithTag(VideoListScreenTags.VIDEO_TITLE).onFirst().assertIsDisplayed()
        composeTestRule.onAllNodesWithTag(VideoListScreenTags.VIDEO_DURATION).onFirst().assertIsDisplayed()
        composeTestRule.onAllNodesWithTag(VideoListScreenTags.VIDEO_STATISTICS).onFirst().assertIsDisplayed()
    }
}