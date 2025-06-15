package com.vlascitchii.data_repository.repository_impl

import com.vlascitchii.data_repository.data_source.local.LocalVideoListDataSource
import com.vlascitchii.data_repository.data_source.remote.RemoteShortsDataSource
import com.vlascitchii.data_repository.util.DispatcherTestRule
import com.vlascitchii.data_repository.util.TestPagingDataDiffer
import com.vlascitchii.data_repository.util.assertListEqualsTo
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
class ShortsRepositoryImplTest {

    @get:Rule
    var dispatcherTestRule = DispatcherTestRule()

    private val remoteShortsDataSource: RemoteShortsDataSource = mock()
    private val localVideoListDataSource: LocalVideoListDataSource = mock()
    private val shortsRepositoryImpl =
        ShortsRepositoryImpl(remoteShortsDataSource, localVideoListDataSource)

    private val initialPageToken = ""
    private val expectedResult = flowOf(RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG)
    private val testPagingDataDiffer = TestPagingDataDiffer(dispatcherTestRule.testDispatcher).pagingDiffer


    @Before
    fun init() {
        whenever(remoteShortsDataSource.fetchShorts(initialPageToken))
            .thenReturn(expectedResult)
    }

    @Test
    fun fun_getShorts_returns_correct_Flow_with_YoutubeVideoResponse() = runTest {
        val actualValue = shortsRepositoryImpl.getShorts().first()
        val testJob = launch { testPagingDataDiffer.submitData(actualValue) }

        advanceUntilIdle()
        testJob.cancel()

        RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG.items.assertListEqualsTo(testPagingDataDiffer.snapshot())
    }

    @Test
    fun fun_getShorts_returns_Flow_which_is_not_empty() = runTest {
        val actualValue = shortsRepositoryImpl.getShorts().first()
        val testJob = launch { testPagingDataDiffer.submitData(actualValue) }

        advanceUntilIdle()
        testJob.cancel()

        assertTrue(testPagingDataDiffer.snapshot().isNotEmpty())
    }
}