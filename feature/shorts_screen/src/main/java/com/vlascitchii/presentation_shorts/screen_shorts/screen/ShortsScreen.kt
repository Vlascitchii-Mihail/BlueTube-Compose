package com.vlascitchii.presentation_shorts.screen_shorts.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.semantics
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.vlascitchii.presentation_common.model.videos.YoutubeVideoUiModel
import com.vlascitchii.presentation_common.ui.screen.CommonScreen
import com.vlascitchii.presentation_common.ui.screen.PagerContentManager
import com.vlascitchii.presentation_common.ui.screen.mvi.MviHandler
import com.vlascitchii.presentation_common.ui.state_common.UiState
import com.vlascitchii.presentation_shorts.screen_shorts.state.ShortsAction
import com.vlascitchii.presentation_shorts.screen_shorts.state.ShortsUIEvent
import com.vlascitchii.presentation_shorts.screen_shorts.state.ShortsUiState
import com.vlascitchii.presentation_shorts.screen_shorts.ui.ShortsList
import com.vlascitchii.shorts_screen.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow

const val SHORTS_BOTTOM_NAV_NAME: String = "Shorts"

@Composable
fun ShortsScreen(
    shortsStateFlow: StateFlow<ShortsUiState>,
    videoQueue: MutableSharedFlow<YouTubePlayer?>,
    shortsMviHandler: MviHandler<ShortsAction, ShortsUIEvent>,
) {
    Surface {
        shortsStateFlow.collectAsStateWithLifecycle().value.let { shortsUiState: ShortsUiState ->
            if (shortsUiState.shortsState == UiState.Loading) shortsMviHandler.submitAction(
                ShortsAction.FetchShortsAction
            )

            CommonScreen(shortsUiState.shortsState) { pagingData: Flow<PagingData<YoutubeVideoUiModel>> ->
                val shorts = pagingData.collectAsLazyPagingItems()

                PagerContentManager(
                    videoState = shorts,
                    contentList = {
                        ShortsList(
                            videos = shorts,
                            videoQueue = videoQueue,
                            shortsMviHandler,
                            connectivityStatus = shortsUiState.networkConnectivityStatus
                        )
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
