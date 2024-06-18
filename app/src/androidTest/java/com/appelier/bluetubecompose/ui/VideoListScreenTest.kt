package com.appelier.bluetubecompose.ui

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import androidx.paging.PagingData
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.appelier.bluetubecompose.MainActivity
import com.appelier.bluetubecompose.R
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideo
import com.appelier.bluetubecompose.screen_video_list.screen.VideoListScreen
import com.appelier.bluetubecompose.utils.VideoListScreenTags
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class VideoListScreenTest {

    @get:Rule(order  = 0)
    val composeAndroidTestRule = createAndroidComposeRule(MainActivity::class.java)
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    private val pd = PagingData.from(YoutubeVideo.DEFAULT_VIDEO_LIST)
    private val videos = MutableStateFlow(pd)

    @Before
    fun init_video_list_screen() {
        composeAndroidTestRule.activity.setContent {
            VideoListScreen(navController = rememberNavController(), videos = videos, searchedVideos = videos)
        }
    }

    @Test
    fun app_shows_appbar() {
        composeAndroidTestRule.onNodeWithText(composeAndroidTestRule.activity.getString(R.string.appbar_title)).assertIsDisplayed()
    }

    @Test
    fun app_shows_video_list() {
        composeAndroidTestRule.onNodeWithTag(VideoListScreenTags.VIDEO_LIST).assertIsDisplayed()
        composeAndroidTestRule.onAllNodesWithTag(VideoListScreenTags.VIDEO_PREVIEW_IMG).onFirst().assertIsDisplayed()
        composeAndroidTestRule.onAllNodesWithTag(VideoListScreenTags.CHANNEL_PREVIEW_IMG).onFirst().assertIsDisplayed()
        composeAndroidTestRule.onAllNodesWithTag(VideoListScreenTags.VIDEO_TITLE).onFirst().assertIsDisplayed()
        composeAndroidTestRule.onAllNodesWithTag(VideoListScreenTags.VIDEO_DURATION).onFirst().assertIsDisplayed()
        composeAndroidTestRule.onAllNodesWithTag(VideoListScreenTags.VIDEO_STATISTICS).onFirst().assertIsDisplayed()
    }
}