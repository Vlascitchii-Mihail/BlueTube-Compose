package com.vlascitchii.data_local.source

import com.vlascitchii.data_local.database.convertToLocalYoutubeVideoResponseEntity
import com.vlascitchii.data_local.enetity.video_list.videos.YoutubeVideoResponseEntity.Companion.testDateTime
import com.vlascitchii.data_local.source.utils.DatabaseContentManager
import com.vlascitchii.data_local.source.utils.rule.DispatcherTestRule
import com.vlascitchii.data_repository.data_source.local.LocalVideoListDataSource
import com.vlascitchii.domain.custom_coroutine_scopes.AppCoroutineScope
import com.vlascitchii.domain.custom_coroutine_scopes.VideoCoroutineScope
import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideoResponse.Companion.RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class LocalVideoListDataSourceImplTest {

    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    private val videoCoroutineScope: AppCoroutineScope =
        VideoCoroutineScope(dispatcher = dispatcherTestRule.testDispatcher)
    private val databaseContentManager: DatabaseContentManager = mock()
    private val localVideoListDataSource: LocalVideoListDataSource =
        LocalVideoListDataSourceImpl(videoCoroutineScope, databaseContentManager)
    private val testDataRemoteResponse =
        RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG.convertToLocalYoutubeVideoResponseEntity()

    @Test
    fun `LocalVideoListDataSourceImpl calls fun setCurrentPageTokenToVideos() from DatabaseContentManager`() {
        localVideoListDataSource.insertVideosToDatabaseWithTimeStamp(
            RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG,
            testDateTime
        )

        with(verify(databaseContentManager)) {
            testDataRemoteResponse.setCurrentPageTokenToVideos(databaseContentManager.sourceCurrentPageToken)
        }
    }

    @Test
    fun `fun insertPageFrom() is called in DatabaseContentManager`() = runTest {
        localVideoListDataSource.insertVideosToDatabaseWithTimeStamp(
            RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG,
            testDateTime
        )

        advanceUntilIdle()
        verify(databaseContentManager).insertPageFrom(anyOrNull())
    }




    @Test
    fun `deleteExtraVideos() is called from LocalVideoListDataSourceImpl`() = runTest {
        localVideoListDataSource.insertVideosToDatabaseWithTimeStamp(
            RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG,
            testDateTime
        )

        advanceUntilIdle()
        verify(databaseContentManager).deleteExtraVideos()
    }

    @Test
    fun `updateCurrentPageToken() updates sourceCurrentPageToken in DatabaseContentManager`() {
        localVideoListDataSource.insertVideosToDatabaseWithTimeStamp(
            RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG,
            testDateTime
        )

        verify(databaseContentManager).updateCurrentPageToken(anyOrNull())
    }
}