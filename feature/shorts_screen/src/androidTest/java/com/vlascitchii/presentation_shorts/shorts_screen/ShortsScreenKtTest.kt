package com.vlascitchii.presentation_shorts.shorts_screen

import android.content.Context
import android.net.ConnectivityManager
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onFirst
import androidx.paging.PagingData
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vlascitchii.domain.model.videos.YoutubeVideoDomain
import com.vlascitchii.domain.usecase.ShortsUseCase
import com.vlascitchii.domain.usecase.UseCase
import com.vlascitchii.domain.util.VideoResult
import com.vlascitchii.presentation_common.model.util.CommonResultConverter
import com.vlascitchii.presentation_common.model.videos.YoutubeVideoUiModel
import com.vlascitchii.presentation_common.network_observer.NetworkConnectivityAbstraction
import com.vlascitchii.presentation_common.network_observer.NetworkConnectivityObserver
import com.vlascitchii.presentation_common.ui.state_common.UiSingleEvent
import com.vlascitchii.presentation_common.ui.state_common.UiState
import com.vlascitchii.presentation_shorts.TestShortsActivity
import com.vlascitchii.presentation_shorts.model.TestVideoUIModel
import com.vlascitchii.presentation_shorts.screen_shorts.screen.ShortsMviHandler
import com.vlascitchii.presentation_shorts.screen_shorts.screen.ShortsScreen
import com.vlascitchii.presentation_shorts.screen_shorts.screen.ShortsViewModel
import com.vlascitchii.shorts_screen.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.spy
import org.mockito.kotlin.whenever

@RunWith(AndroidJUnit4::class)
class ShortsScreenKtTest {

    @get:Rule
    val composeActivityTestRule: AndroidComposeTestRule<ActivityScenarioRule<TestShortsActivity>, TestShortsActivity> = createAndroidComposeRule(TestShortsActivity::class.java)
    private val shortsUseCase: UseCase<ShortsUseCase.ShortsRequest, ShortsUseCase.ShortsResponse> = mock()
    private val shortsConverter: CommonResultConverter<ShortsUseCase.ShortsResponse, @JvmSuppressWildcards Flow<PagingData<YoutubeVideoUiModel>>> = mock()
    private val context: Context = ApplicationProvider.getApplicationContext<Context>()
    private val networkConnectivityObserver: NetworkConnectivityAbstraction =
        NetworkConnectivityObserver(
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        )
    private lateinit var shortsViewModel: ShortsViewModel
    private lateinit var mviHandler: ShortsMviHandler

    private val pagingUiData: Flow<PagingData<YoutubeVideoUiModel>> =
        flowOf(PagingData.Companion.from(TestVideoUIModel.TestUIData.TEST_UI_VIDEO_LIST))
    private val pagingData: Flow<PagingData<YoutubeVideoDomain>> =
        flowOf(PagingData.Companion.from(emptyList()))

    private val expectedRelatedVideosUseCaseResponse: VideoResult<ShortsUseCase.ShortsResponse> =
        VideoResult.Success(ShortsUseCase.ShortsResponse(pagingData))
    private val successConverterUiState: UiState<Flow<PagingData<YoutubeVideoUiModel>>> =
        UiState.Success(pagingUiData)

    private val sortsVideoPlayerDescription: String = context.getString(R.string.shorts_video_description)
    private val shortsTitle: String = context.getString(R.string.shorts_title)
    private val shortsChannelIconDescription: String = context.getString(R.string.shorts_channel_icon_description)

    @Before
    fun setup() {
        shortsViewModel = ShortsViewModel(
            shortsUseCase,
            shortsConverter,
            networkConnectivityObserver,
            Dispatchers.IO
        )
        mviHandler = spy(
            ShortsMviHandler(
                shortsViewModel,
                navigationHandler = { event: UiSingleEvent -> Unit }
            )
        )

        whenever(shortsUseCase.execute(any<ShortsUseCase.ShortsRequest>()))
            .thenReturn(flowOf(expectedRelatedVideosUseCaseResponse))
        whenever(shortsConverter.convertSuccessVideo(any<Flow<PagingData<YoutubeVideoDomain>>>()))
            .thenReturn(successConverterUiState)
        whenever(shortsConverter.convert(any<VideoResult<ShortsUseCase.ShortsResponse>>()))
            .thenReturn(successConverterUiState)

        composeActivityTestRule.setContent {
            ShortsScreen(
                shortsViewModel.shortsUiStateFlow,
                shortsViewModel.videoQueue,
                mviHandler
            )
        }
    }

    @Test
    fun shorts_item_is_displayed() {
        with(composeActivityTestRule) {
            onAllNodesWithContentDescription(sortsVideoPlayerDescription).onFirst().assertIsDisplayed()
            onAllNodesWithContentDescription(shortsTitle).onFirst().assertIsDisplayed()
            onAllNodesWithContentDescription(shortsChannelIconDescription).onFirst().assertIsDisplayed()
        }
    }
}
