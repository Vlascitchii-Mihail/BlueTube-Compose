package com.vlascitchii.presentation_video_list.screen

import android.content.Context
import android.net.ConnectivityManager
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.paging.PagingData
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vlascitchii.domain.model.videos.YoutubeVideoDomain
import com.vlascitchii.domain.usecase.UseCase
import com.vlascitchii.domain.usecase.VideoListUseCase
import com.vlascitchii.domain.util.VideoResult
import com.vlascitchii.presentation_common.model.util.CommonResultConverter
import com.vlascitchii.presentation_common.model.videos.YoutubeVideoUiModel
import com.vlascitchii.presentation_common.network_observer.NetworkConnectivityAbstraction
import com.vlascitchii.presentation_common.network_observer.NetworkConnectivityObserver
import com.vlascitchii.presentation_common.ui.navigation_common.BlueTubeBottomNavigation
import com.vlascitchii.presentation_common.ui.state_common.UiState
import com.vlascitchii.presentation_common.ui.video_list.state.VideoListNavigationEvent
import com.vlascitchii.presentation_video_list.TestVideoListActivity
import com.vlascitchii.presentation_video_list.model.TestVideoUIModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import com.vlascitchii.common_ui.R as CommonR
import com.vlascitchii.video_list_screen.R as VideoListR


@RunWith(AndroidJUnit4::class)
class VideoListScreenTest {
    @get:Rule
    // try to exclude activity and use createComposeRule()
    val composeAndroidTestRule: AndroidComposeTestRule<ActivityScenarioRule<TestVideoListActivity>, TestVideoListActivity> =
        createAndroidComposeRule(TestVideoListActivity::class.java)

    private val videoListUseCase: UseCase<VideoListUseCase.VideoListRequest, VideoListUseCase.VideoListResponse> = mock()
    private val videoListConverter: CommonResultConverter<VideoListUseCase.VideoListResponse, @JvmSuppressWildcards Flow<PagingData<YoutubeVideoUiModel>>> = mock()
    private val context: Context = ApplicationProvider.getApplicationContext<Context>()
    private val networkConnectivityObserver: NetworkConnectivityAbstraction =
        NetworkConnectivityObserver(
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        )
    private lateinit var videoListViewModel: VideoListViewModel
    private lateinit var mviHandler: VideoListMviHandler

    private val pagingUiData: Flow<PagingData<YoutubeVideoUiModel>> =
        flowOf(PagingData.Companion.from(TestVideoUIModel.TestUIData.TEST_UI_VIDEO_LIST))
    private val pagingData: Flow<PagingData<YoutubeVideoDomain>> =
        flowOf(PagingData.Companion.from(emptyList()))

    private val expectedVideoListResponse: VideoResult<VideoListUseCase.VideoListResponse> =
        VideoResult.Success(VideoListUseCase.VideoListResponse(pagingData))
    private val successConvertedUiState: UiState<Flow<PagingData<YoutubeVideoUiModel>>> =
        UiState.Success(pagingUiData)

    private val blueTubeTopAppBarDescription: String = context.getString(VideoListR.string.appbar_title)
    private val searchAppBarDescription: String = context.getString(VideoListR.string.search_app_bar_description)
    private val searchIconDescription: String = context.getString(VideoListR.string.appbar_search_icon_descr)
    private val closeSearchIconDescription: String = context.getString(VideoListR.string.appbar_close_icon_descr)
    private val videoListDescription: String = context.getString(CommonR.string.video_list_description)
    private val bottomNavigationBarDescription: String = context.getString(CommonR.string.bottom_navigation_description)
    private val testSearchQuery: String = ""

    @Before
    fun setup() {
        videoListViewModel = VideoListViewModel(
            videoListUseCase,
            videoListConverter,
            networkConnectivityObserver,
            Dispatchers.IO
        )

        mviHandler = VideoListMviHandler(
            videoListViewModel,
            navigationHandler = { event: VideoListNavigationEvent -> Unit }
        )

        whenever(videoListUseCase.execute(any<VideoListUseCase.VideoListRequest>()))
            .thenReturn(flowOf(expectedVideoListResponse))
        whenever(videoListConverter.convertSuccessVideo(any<Flow<PagingData<YoutubeVideoDomain>>>()))
            .thenReturn(successConvertedUiState)
        whenever(videoListConverter.convert(any<VideoResult<VideoListUseCase.VideoListResponse>>()))
            .thenReturn(successConvertedUiState)

        composeAndroidTestRule.setContent {
            VideoListScreen(
                videoListViewModel.videoListUIStateFlow,
                mviHandler,
                testSearchQuery,
                Modifier,
                bottomNavigation = { BlueTubeBottomNavigation() }
            )
        }
    }

    @Test
    fun `VideoListScreen displays basic screens elements`() {
        with(composeAndroidTestRule) {
            onNodeWithContentDescription(blueTubeTopAppBarDescription).assertIsDisplayed()
            waitForIdle()
            onNodeWithContentDescription(videoListDescription).assertIsDisplayed()
            onNodeWithContentDescription(bottomNavigationBarDescription).assertIsDisplayed()
        }
    }

    @Test
    fun `AppBar switches to search bar`() {
        with(composeAndroidTestRule) {
            onNodeWithContentDescription(searchIconDescription).performClick()
            onNodeWithContentDescription(searchAppBarDescription).assertIsDisplayed()
        }
    }

    @Test
    fun `SearchAppBar switches to AppBar`() {
        with(composeAndroidTestRule) {
            onNodeWithContentDescription(searchIconDescription).performClick()
            onNodeWithContentDescription(closeSearchIconDescription).performClick()
            onNodeWithContentDescription(searchAppBarDescription).assertIsNotDisplayed()
            onNodeWithContentDescription(blueTubeTopAppBarDescription).assertIsDisplayed()
        }
    }
}
