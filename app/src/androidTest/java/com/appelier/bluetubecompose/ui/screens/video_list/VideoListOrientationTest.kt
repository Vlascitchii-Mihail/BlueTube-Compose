package com.appelier.bluetubecompose.ui.screens.video_list

import androidx.activity.compose.setContent
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onFirst
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.test.espresso.device.DeviceInteraction.Companion.setScreenOrientation
import androidx.test.espresso.device.EspressoDevice.Companion.onDevice
import androidx.test.espresso.device.action.ScreenOrientation
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.appelier.bluetubecompose.MainActivity
import com.appelier.bluetubecompose.R
import com.appelier.bluetubecompose.core.core_ui.views.video_list_screen.ItemsList
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideo.Companion.DEFAULT_VIDEO_LIST
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class VideoListOrientationTest {

    @get:Rule
    val composeAndroidTestRule = createAndroidComposeRule(MainActivity::class.java)
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    private val videoPage = MutableStateFlow(PagingData.from(DEFAULT_VIDEO_LIST))

    @Before
    fun initItemList() {
        hiltRule.inject()
        initPortraitList()
    }

    private fun initPortraitList() {
        composeAndroidTestRule.activity.setContent {
            ItemsList(
                videos = videoPage.collectAsLazyPagingItems(),
                modifier = Modifier,
                navigateToPlayerScreen = {},
                windowSize = WindowWidthSizeClass.Compact
            )
        }
    }

    private fun initLandscapeList() {
        composeAndroidTestRule.activity.setContent {
            ItemsList(
                videos = videoPage.collectAsLazyPagingItems(),
                modifier = Modifier,
                navigateToPlayerScreen = {},
                windowSize = WindowWidthSizeClass.Medium
            )
        }
    }

    @Test
    fun onChangeOrientation_showDifferentConfigurations_of_VideoList() {
        with(composeAndroidTestRule) {
            initPortraitList()
            onAllNodes(hasClickableLabel(activity.getString(R.string.video_compact_preview))).onFirst().assertIsDisplayed()
            changeOrientation(ScreenOrientation.LANDSCAPE)
            initLandscapeList()
            onAllNodes(hasClickableLabel(activity.getString(R.string.video_medium_preview))).onFirst().assertIsDisplayed()
            changeOrientation(ScreenOrientation.PORTRAIT)
            initPortraitList()
            onAllNodes(hasClickableLabel(activity.getString(R.string.video_compact_preview))).onFirst().assertIsDisplayed()
        }
    }

    private fun changeOrientation(newOrientation: ScreenOrientation) {
        onDevice().setScreenOrientation(newOrientation)
    }

    private fun hasClickableLabel(label: String) =
        SemanticsMatcher("Clickable action with label: $label") {
            it.config.getOrNull(SemanticsActions.OnClick)?.label == label
        }
}