package com.vlascitchii.data_repository.repository_impl

import com.vlascitchii.common_test.rule.DispatcherTestRule
import com.vlascitchii.common_test.util.assertListEqualsTo
import com.vlascitchii.data_repository.data_source.local.LocalVideoListDataSource
import com.vlascitchii.data_repository.data_source.remote.RemoteSearchDataSource
import com.vlascitchii.data_repository.data_source.remote.RemoteVideoListDataSource
import com.vlascitchii.common_test_android.TestPagingDomainYouTubeVideoDiffer
import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideoResponse.Companion.RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class VideoListRepositoryImplTest {

    @get:Rule
    var dispatcherTestRule = DispatcherTestRule()

    private val remoteVideoListDataSource: RemoteVideoListDataSource = mock()
    private val remoteSearchDataSource: RemoteSearchDataSource = mock()
    private val localVideoListDataSource: LocalVideoListDataSource = mock()

    private val videoListRepositoryImpl = VideoListRepositoryImpl(
        remoteVideoListDataSource,
        remoteSearchDataSource,
        localVideoListDataSource
    )

    private val initialPageToken = ""
    private val testQuery = "Test query"
    private val expectedDataSourceResult = flowOf(RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG)
    private val testPagingDomainYouTubeVideoDiffer = TestPagingDomainYouTubeVideoDiffer(dispatcherTestRule.testDispatcher).pagingDiffer

    @Before
    fun init() {
        whenever(remoteVideoListDataSource.fetchVideos(initialPageToken))
            .thenReturn(expectedDataSourceResult)

        whenever(remoteSearchDataSource.searchVideos(testQuery, initialPageToken))
            .thenReturn(expectedDataSourceResult)
    }

    @Test
    fun fun_getPopularVideos_returns_Flow_which_is_not_empty() = runTest {
        val pagingData = videoListRepositoryImpl.getPopularVideos().first()
        val testJob = launch { testPagingDomainYouTubeVideoDiffer.submitData(pagingData) }

        advanceUntilIdle()
        testJob.cancel()

        assertTrue(testPagingDomainYouTubeVideoDiffer.snapshot().isNotEmpty())
    }

    @Test
    fun fun_getPopularVideos_returns_correctFlow_with_PagingData_YoutubeVideo() = runTest {
        val pagingData = videoListRepositoryImpl.getPopularVideos().first()
        val testJob = launch { testPagingDomainYouTubeVideoDiffer.submitData(pagingData) }

        advanceUntilIdle()
        testJob.cancel()

        RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG.items.assertListEqualsTo(testPagingDomainYouTubeVideoDiffer.snapshot())
    }

    @Test
    fun fun_getSearchVideos_returns_Flow_which_is_not_empty() = runTest {
        val pagingData = videoListRepositoryImpl.getSearchVideos(testQuery).first()
        val testJob = launch { testPagingDomainYouTubeVideoDiffer.submitData(pagingData) }

        advanceUntilIdle()
        testJob.cancel()

        assertTrue(testPagingDomainYouTubeVideoDiffer.snapshot().isNotEmpty())
    }

    @Test
    fun fun_getSearchVideos_returns_videos_by_query() = runTest {
        val pagingData = videoListRepositoryImpl.getSearchVideos(testQuery).first()
        val testJob = launch { testPagingDomainYouTubeVideoDiffer.submitData(pagingData) }

        advanceUntilIdle()
        testJob.cancel()

        RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG.items.assertListEqualsTo(testPagingDomainYouTubeVideoDiffer.snapshot())
    }
}