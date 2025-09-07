package com.vlascitchii.data_repository.repository_impl

import androidx.recyclerview.widget.DiffUtil
import com.vlascitchii.common_test.paging.CommonTestPagingDiffer
import com.vlascitchii.common_test.rule.DispatcherTestRule
import com.vlascitchii.common_test.util.assertListEqualsTo
import com.vlascitchii.data_repository.data_source.local.LocalVideoListDataSource
import com.vlascitchii.data_repository.data_source.remote.RemoteSearchDataSource
import com.vlascitchii.data_repository.data_source.remote.RemoteVideoListDataSource
import com.vlascitchii.data_repository.mock_model.DOMAIN_RESPONSE_VIDEO_WITH_CHANNEL_IMG
import com.vlascitchii.domain.custom_scope.CustomCoroutineScope
import com.vlascitchii.domain.model.videos.YoutubeVideoDomain
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.wheneverBlocking

@OptIn(ExperimentalCoroutinesApi::class)
class VideoListRepositoryImplTest {

    @get:Rule
    var dispatcherTestRule = DispatcherTestRule()

    private val remoteVideoListDataSource: RemoteVideoListDataSource = mock()
    private val remoteSearchDataSource: RemoteSearchDataSource = mock()
    private val localVideoListDataSource: LocalVideoListDataSource = mock()
    private val customCoroutineScope: CustomCoroutineScope = CustomCoroutineScope(dispatcherTestRule.testDispatcher)

    private val videoListRepositoryImpl = VideoListRepositoryImpl(
        remoteVideoListDataSource,
        remoteSearchDataSource,
        localVideoListDataSource,
        customCoroutineScope
    )

    private val initialPageToken = ""
    private val testQuery = "Test query"
    private val expectedDataSourceResult = DOMAIN_RESPONSE_VIDEO_WITH_CHANNEL_IMG
    private val differCallback = object : DiffUtil.ItemCallback<YoutubeVideoDomain>() {

        override fun areItemsTheSame(oldItem: YoutubeVideoDomain, newItem: YoutubeVideoDomain): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: YoutubeVideoDomain, newItem: YoutubeVideoDomain): Boolean {
            return oldItem == newItem
        }
    }
    private val testPagingDomainYouTubeVideoDiffer =
        CommonTestPagingDiffer(dispatcherTestRule.testDispatcher, differCallback).pagingDiffer

    @Before
    fun init() {
        wheneverBlocking { remoteVideoListDataSource.fetchVideos(initialPageToken) }
            .thenReturn(expectedDataSourceResult)

        wheneverBlocking {remoteSearchDataSource.searchVideos(testQuery, initialPageToken) }
            .thenReturn(expectedDataSourceResult)
    }

    @Test
    fun `fun getPopularVideos inserts videos into the DB`() = runTest {
        val pagingData = videoListRepositoryImpl.getPopularVideos().first()
        val testJob = launch { testPagingDomainYouTubeVideoDiffer.submitData(pagingData) }

        advanceUntilIdle()
        testJob.cancel()

        assertTrue(testPagingDomainYouTubeVideoDiffer.snapshot().isNotEmpty())
        verify(localVideoListDataSource).insertVideosToDatabaseWithTimeStamp(any(), any())
    }

    @Test
    fun `fun getPopularVideos returns correct Flow with PagingData YoutubeVideo`() = runTest {
        val pagingData = videoListRepositoryImpl.getPopularVideos().first()
        val testJob = launch { testPagingDomainYouTubeVideoDiffer.submitData(pagingData) }

        advanceUntilIdle()
        testJob.cancel()

        DOMAIN_RESPONSE_VIDEO_WITH_CHANNEL_IMG.items.assertListEqualsTo(testPagingDomainYouTubeVideoDiffer.snapshot())
    }

    @Test
    fun `fun getSearchVideos inserts videos into the DB`() = runTest {
        val pagingData = videoListRepositoryImpl.getSearchVideos(testQuery).first()
        val testJob = launch { testPagingDomainYouTubeVideoDiffer.submitData(pagingData) }

        advanceUntilIdle()
        testJob.cancel()

        assertTrue(testPagingDomainYouTubeVideoDiffer.snapshot().isNotEmpty())
        verify(localVideoListDataSource).insertVideosToDatabaseWithTimeStamp(any(), any())
    }

    @Test
    fun fun_getSearchVideos_returns_videos_by_query() = runTest {
        val pagingData = videoListRepositoryImpl.getSearchVideos(testQuery).first()
        val testJob = launch { testPagingDomainYouTubeVideoDiffer.submitData(pagingData) }

        advanceUntilIdle()
        testJob.cancel()

        DOMAIN_RESPONSE_VIDEO_WITH_CHANNEL_IMG.items.assertListEqualsTo(testPagingDomainYouTubeVideoDiffer.snapshot())
    }
}
