package com.vlascitchii.data_local.instrumented

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vlascitchii.data_local.database.YouTubeDatabase
import com.vlascitchii.data_local.mock_model.DOMAIN_RESPONSE_VIDEO_WITH_CHANNEL_IMG
import com.vlascitchii.data_local.mock_model.testDateTime
import com.vlascitchii.data_local.source.DatabaseVideoSourceImpl
import com.vlascitchii.data_local.source.utils.DatabaseContentManager
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject
import kotlin.math.exp

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class DatabaseVideoSourceImplTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var inMemoryDatabase: YouTubeDatabase

    @Inject
    lateinit var databaseContentManager: DatabaseContentManager
    private lateinit var localVideoListDataSourceImpl: DatabaseVideoSourceImpl

    @Before
    fun init() {
        hiltRule.inject()

        localVideoListDataSourceImpl =

                DatabaseVideoSourceImpl(
                    youTubeDatabase = inMemoryDatabase,
                    databaseContentManager = databaseContentManager
                )

    }

    @Test
    fun fun_insertVideosToDatabaseWithTimeStamp_inserts_and_fun_getVideosFromDatabase_gets_YoutubeVideoResponseEntity() =
        runBlocking {
            val emptyPageToken = ""
            val expectedYouTubeVideoResponse = DOMAIN_RESPONSE_VIDEO_WITH_CHANNEL_IMG

            localVideoListDataSourceImpl.insertVideosWithTimeStamp(
                expectedYouTubeVideoResponse,
                testDateTime
            )

            val actualYouTubeVideoResponse = localVideoListDataSourceImpl
                .getVideosFromStore(emptyPageToken)
                .first()

            assertEquals(expectedYouTubeVideoResponse, actualYouTubeVideoResponse)
            assertEquals(expectedYouTubeVideoResponse.nextPageToken, databaseContentManager.sourceCurrentPageToken)
        }

    @Test
    fun inserting_2_pages_setting_the_nextPageToken_from_the_first_page_as_currentPageToken_for_the_second_page() =
        runBlocking {
            val expectedYouTubeVideoResponse = DOMAIN_RESPONSE_VIDEO_WITH_CHANNEL_IMG
            val firstPageNextPageToken = expectedYouTubeVideoResponse.nextPageToken

            localVideoListDataSourceImpl.insertVideosWithTimeStamp(
                expectedYouTubeVideoResponse,
                testDateTime
            )

            localVideoListDataSourceImpl.insertVideosWithTimeStamp(
                expectedYouTubeVideoResponse,
                testDateTime
            )

            val secondPageFromDatabase = localVideoListDataSourceImpl
                .getVideosFromStore(firstPageNextPageToken)
                .first()

            assertEquals(expectedYouTubeVideoResponse.nextPageToken, databaseContentManager.sourceCurrentPageToken)
            assertEquals(expectedYouTubeVideoResponse.nextPageToken, secondPageFromDatabase.currentPageToken)
        }
}
