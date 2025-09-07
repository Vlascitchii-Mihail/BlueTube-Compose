package com.vlascitchii.domain.usecase

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.DiffUtil
import com.vlascitchii.common_test.paging.CommonTestPagingDiffer
import com.vlascitchii.common_test.rule.DispatcherTestRule
import com.vlascitchii.common_test.util.assertListEqualsTo
import com.vlascitchii.domain.mock_model.DOMAIN_RESPONSE_VIDEO_WITH_CHANNEL_IMG
import com.vlascitchii.domain.model.videos.YoutubeVideoDomain
import com.vlascitchii.domain.repository.PlayerRepository
import com.vlascitchii.domain.usecase.util.DispatcherConfiguration
import com.vlascitchii.domain.util.VideoResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class VideoPlayerUseCaseTest {

    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    private val configuration: DispatcherConfiguration = mock()
    private val playerRepository: PlayerRepository = mock()
    private lateinit var videoPlayerUseCase: VideoPlayerUseCase

    private val repositoryExpectedResultFlow: Flow<PagingData<YoutubeVideoDomain>> =
        flowOf(PagingData.from((DOMAIN_RESPONSE_VIDEO_WITH_CHANNEL_IMG.items)))

    private val positiveExpectedResult: VideoResult.Success<VideoPlayerUseCase.PlayerResponse> =
        VideoResult.Success(VideoPlayerUseCase.PlayerResponse(repositoryExpectedResultFlow))

    private val differCallback = object : DiffUtil.ItemCallback<YoutubeVideoDomain>() {

        override fun areItemsTheSame(oldItem: YoutubeVideoDomain, newItem: YoutubeVideoDomain): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: YoutubeVideoDomain, newItem: YoutubeVideoDomain): Boolean {
            return oldItem == newItem
        }
    }
    private lateinit var expectedTestPagingDomainYouTubeVideoDiffer: AsyncPagingDataDiffer<YoutubeVideoDomain>
    private lateinit var actualTestPagingDomainYouTubeVideoDiffer : AsyncPagingDataDiffer<YoutubeVideoDomain>

    @Before
    fun init() {
        whenever(configuration.dispatcher).thenReturn(dispatcherTestRule.testDispatcher)
        videoPlayerUseCase = VideoPlayerUseCase(configuration, playerRepository)
        expectedTestPagingDomainYouTubeVideoDiffer =
            CommonTestPagingDiffer(dispatcherTestRule.testDispatcher, differCallback).pagingDiffer
        actualTestPagingDomainYouTubeVideoDiffer =
            CommonTestPagingDiffer(dispatcherTestRule.testDispatcher, differCallback).pagingDiffer
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `fun execute() returns Success SearchRelatedVideosResult with pager inside`() = runTest {
        whenever(playerRepository.getSearchRelayedVideos("Test query"))
            .thenReturn(repositoryExpectedResultFlow)

        val actualResult = videoPlayerUseCase.execute(
            VideoPlayerUseCase.PlayerRequest("Test query", this)
        )

        val expectedPagingData = positiveExpectedResult.data.relatedVideoPagingData.first()
        val actualPagingData = (actualResult.first() as VideoResult.Success).data.relatedVideoPagingData.first()

        val testJob1 = launch { expectedTestPagingDomainYouTubeVideoDiffer.submitData(expectedPagingData) }
        val testJob2 = launch { actualTestPagingDomainYouTubeVideoDiffer.submitData(actualPagingData) }

        advanceUntilIdle()
        testJob1.cancel()
        testJob2.cancel()

        expectedTestPagingDomainYouTubeVideoDiffer.snapshot().assertListEqualsTo(actualTestPagingDomainYouTubeVideoDiffer.snapshot())
    }
}
