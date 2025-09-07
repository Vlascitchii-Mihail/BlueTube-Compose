package com.vlascitchii.presentation_shorts.screen_shorts.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.vlascitchii.presentation_common.model.videos.YoutubeVideoUiModel
import com.vlascitchii.presentation_common.network_observer.NetworkConnectivityStatus
import com.vlascitchii.presentation_common.ui.screen.CommonScreen
import com.vlascitchii.presentation_common.ui.screen.PagerContentManager
import com.vlascitchii.presentation_common.ui.state.UiState
import com.vlascitchii.presentation_common.utils.NavigationTags
import com.vlascitchii.presentation_shorts.screen_shorts.ui.ShortsList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun ShortsScreen(
    shortsStateFlow: StateFlow<UiState<Flow<PagingData<YoutubeVideoUiModel>>>>,
    videoQueue: MutableSharedFlow<YouTubePlayer?>,
    listenToVideoQueue: () -> Unit,
    networkConnectivityStatus: Flow<NetworkConnectivityStatus>,
) {
    Surface(modifier = Modifier.testTag(NavigationTags.SHORTS_SCREEN)) {
        shortsStateFlow.collectAsStateWithLifecycle().value.let { uiStatePagingData: UiState<Flow<PagingData<YoutubeVideoUiModel>>> ->
            CommonScreen(uiStatePagingData) { pagingData: Flow<PagingData<YoutubeVideoUiModel>> ->
                val shorts = pagingData.collectAsLazyPagingItems()

                PagerContentManager(
                    videoState = shorts,
                    contentList = {
                        ShortsList(
                            videos = shorts,
                            videoQueue = videoQueue,
                            listenToVideoQueue,
                            connectivityStatus = networkConnectivityStatus
                        )
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
