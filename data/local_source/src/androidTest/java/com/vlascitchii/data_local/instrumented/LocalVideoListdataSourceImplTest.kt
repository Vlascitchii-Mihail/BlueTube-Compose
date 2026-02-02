package com.vlascitchii.data_local.instrumented

//import androidx.test.ext.junit.runners.AndroidJUnit4
//import com.vlascitchii.data_local.source.LocalVideoListDataSourceImpl
//import com.vlascitchii.data_local.source.utils.DatabaseContentManager
//import com.vlascitchii.domain.custom_coroutine_scopes.AppCoroutineScope
//import com.vlascitchii.domain.custom_coroutine_scopes.VideoCoroutineScope
//import com.vlascitchii.domain.model.videos.YoutubeVideoResponseDomain.Companion.DOMAIN_RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG
//import com.vlascitchii.domain.model.videos.YoutubeVideoResponseDomain.Companion.testDateTime
//import dagger.hilt.android.testing.HiltAndroidRule
//import dagger.hilt.android.testing.HiltAndroidTest
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.flow.first
//import kotlinx.coroutines.runBlocking
//import org.junit.Assert.assertEquals
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//import javax.inject.Inject
//
//@HiltAndroidTest
//@RunWith(AndroidJUnit4::class)
//class LocalVideoListDataSourceImplTest {
//
//    @get:Rule
//    val hiltRule = HiltAndroidRule(this)
//
//    @Inject
//    lateinit var databaseContentManager: DatabaseContentManager
//    private lateinit var localVideoListDataSourceImpl: LocalVideoListDataSourceImpl
//    private lateinit var videoCoroutineScope: AppCoroutineScope
//
//    @Before
//    fun init() {
//        hiltRule.inject()
//
//        videoCoroutineScope = VideoCoroutineScope()
//        localVideoListDataSourceImpl =
//            LocalVideoListDataSourceImpl(videoCoroutineScope, databaseContentManager)
//    }
//
//    @Test
//    fun fun_insertVideosToDatabaseWithTimeStamp_inserts_and_fun_getVideosFromDatabase_gets_YoutubeVideoResponseEntity() = runBlocking {
//        val emptyPageToken = ""
//        val expectedYouTubeVideoResponse = DOMAIN_RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG
//
//        localVideoListDataSourceImpl.insertVideosToDatabaseWithTimeStamp(
//            expectedYouTubeVideoResponse,
//            testDateTime
//        )
//
//        delay(2000)
//        val actualYouTubeVideoResponse = localVideoListDataSourceImpl.getVideosFromDatabase(emptyPageToken).first()
//
//        assertEquals(expectedYouTubeVideoResponse, actualYouTubeVideoResponse)
//    }
//}