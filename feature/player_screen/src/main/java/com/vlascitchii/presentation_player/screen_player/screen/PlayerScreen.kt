package com.vlascitchii.presentation_player.screen_player.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.vlascitchii.presentation_common.model.videos.YoutubeVideoUiModel
import com.vlascitchii.presentation_common.model.videos.YoutubeVideoUiModel.Companion.PREVIEW_VIDEO_LIST
import com.vlascitchii.presentation_common.network_observer.NetworkConnectivityStatus
import com.vlascitchii.presentation_common.ui.global_snackbar.SnackBarController.sendEvent
import com.vlascitchii.presentation_common.ui.global_snackbar.SnackBarEvent
import com.vlascitchii.presentation_common.ui.screen.CommonScreen
import com.vlascitchii.presentation_common.ui.screen.PagerContentManager
import com.vlascitchii.presentation_common.ui.screen.mvi.CommonMVI
import com.vlascitchii.presentation_common.ui.state.UiState
import com.vlascitchii.presentation_common.ui.theme.BlueTubeComposeTheme
import com.vlascitchii.presentation_common.ui.video_list.ItemsList
import com.vlascitchii.player_screen.R
import com.vlascitchii.presentation_player.screen_player.OrientationState
import com.vlascitchii.presentation_player.screen_player.state.PlayerActionState
import com.vlascitchii.presentation_player.screen_player.state.PlayerNavigationEvent
import com.vlascitchii.presentation_player.screen_player.state.PlayerState
import com.vlascitchii.presentation_player.screen_player.ui.VideoDescription
import com.vlascitchii.presentation_player.screen_player.ui.YoutubeVideoPlayer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import com.vlascitchii.common_ui.R as CommonR

const val VIDEO_PLAYER_BOTTOM_NAV_NAME = "Video player"

@Composable
fun PlayerScreen(
    video: YoutubeVideoUiModel,
    playerStateFlow: StateFlow<PlayerState>,
    playerMVI: CommonMVI<PlayerActionState, PlayerNavigationEvent>,
    playbackPosition: Float,
    modifier: Modifier = Modifier,
    isPreview: Boolean = false
) {

    val youTubePlayerDescription = stringResource(R.string.video_player_description)
    val localContext = LocalContext.current

    playerStateFlow.collectAsStateWithLifecycle().value.let { playerUiState: PlayerState ->
        if (playerUiState.relatedVideoState == UiState.Loading) playerMVI.submitAction(
            PlayerActionState.GetRelatedVideosAction(video.snippet.title)
        )

        val isPortrait = playerUiState.playerOrientationState == OrientationState.PORTRAIT

        Column(
            modifier = if (isPortrait)
                modifier
                    .fillMaxSize()
                    .statusBarsPadding()
            else modifier.fillMaxSize()
                .semantics { contentDescription = localContext.getString(R.string.player_screen_description)}
        ) {
            if (isPreview) CreatePlayerPreview(isPortrait, modifier)
            else YoutubeVideoPlayer(
                videoId = video.id,
                modifier = modifier.semantics { contentDescription = youTubePlayerDescription },
                playerStateFlow = playerStateFlow,
                playerMVI = playerMVI,
                playbackPosition = playbackPosition,
            )

            if (isPortrait) VideoDescription(video = video)

            CommonScreen(playerUiState.relatedVideoState) { pagingData: Flow<PagingData<YoutubeVideoUiModel>> ->

                LaunchedEffect(playerUiState.networkConnectivityStatus) {
                    if (playerUiState.networkConnectivityStatus == NetworkConnectivityStatus.Lost) {
                        sendEvent(
                            event = SnackBarEvent(
                                message = "Wrong internet connection"
                            )
                        )
                    }
                }

                if (isPortrait) {
                    val lazyPagingData = pagingData.collectAsLazyPagingItems()

                    PagerContentManager(
                        videoState = lazyPagingData,
                        contentList = {
                            ItemsList(
                                lazyPagingData,
                                modifier,
                                //TODO: Refactor after update VideoListScreen to MVI
                                navigateToPlayerScreen = { video: YoutubeVideoUiModel ->
                                    playerMVI.submitSingleNavigationEvent(
                                        PlayerNavigationEvent.NavigationPlayerScreenEvent(
                                            video
                                        )
                                    )
                                },
                            )
                        },
                        modifier = modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
fun CreatePlayerPreview(isPortrait: Boolean, modifier: Modifier) {
    Column(
        modifier = if (isPortrait) modifier.fillMaxWidth() else modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onBackground)
            .wrapContentHeight()
    ) {
        Spacer(
            modifier = modifier
                .height(dimensionResource(CommonR.dimen.height_large_extra_250))
        )
    }
}

private val pagingUiData: Flow<PagingData<YoutubeVideoUiModel>> =
    flowOf(PagingData.Companion.from(PREVIEW_VIDEO_LIST))
private val successUiState: UiState<Flow<PagingData<YoutubeVideoUiModel>>> =
    UiState.Success(pagingUiData)
val playerStateFlow: MutableStateFlow<PlayerState> =
    MutableStateFlow(PlayerState(relatedVideoState = successUiState))
val playerStateFlowLandscape: MutableStateFlow<PlayerState> =
    MutableStateFlow(
        PlayerState(
            relatedVideoState = successUiState,
            playerOrientationState = OrientationState.FULL_SCREEN
        )
    )

@PreviewLightDark
@Composable
fun PlayerScreenPortraitPreview() {

    val playerMVI: CommonMVI<PlayerActionState, PlayerNavigationEvent> =
        object : CommonMVI<PlayerActionState, PlayerNavigationEvent>() {
            override fun handleAction(action: PlayerActionState) {}
            override fun handleNavigationEvent(singleEvent: PlayerNavigationEvent) {}
        }

    BlueTubeComposeTheme {
        Surface {
            PlayerScreen(
                video = PREVIEW_VIDEO_LIST.first(),
                playerStateFlow = playerStateFlow,
                playerMVI = playerMVI,
                playbackPosition = 0F,
                isPreview = true
            )
        }
    }
}

@Preview(device = "spec:parent=pixel_9_pro_xl,orientation=landscape")
@Composable
fun PlayerScreenLandscapePreview() {
    val playerMVI: CommonMVI<PlayerActionState, PlayerNavigationEvent> =
        object : CommonMVI<PlayerActionState, PlayerNavigationEvent>() {
            override fun handleAction(action: PlayerActionState) {}
            override fun handleNavigationEvent(singleEvent: PlayerNavigationEvent) {}
        }

    BlueTubeComposeTheme {
        Surface {
            PlayerScreen(
                video = PREVIEW_VIDEO_LIST.first(),
                playerStateFlow = playerStateFlowLandscape,
                playerMVI = playerMVI,
                playbackPosition = 0F,
                isPreview = true
            )
        }
    }
}

@Preview(device = "spec:parent=pixel_tablet,orientation=portrait")
@Composable
fun PlayerScreenTabletPortraitPreview() {
    val playerMVI: CommonMVI<PlayerActionState, PlayerNavigationEvent> =
        object : CommonMVI<PlayerActionState, PlayerNavigationEvent>() {
            override fun handleAction(action: PlayerActionState) {}
            override fun handleNavigationEvent(singleEvent: PlayerNavigationEvent) {}
        }

    BlueTubeComposeTheme {
        Surface {
            PlayerScreen(
                video = PREVIEW_VIDEO_LIST.first(),
                playerStateFlow = playerStateFlow,
                playerMVI = playerMVI,
                playbackPosition = 0F,
                isPreview = true
            )
        }
    }
}

@Preview(device = "spec:parent=pixel_tablet,orientation=landscape")
@Composable
fun PlayerScreenTabletLandscapePreview() {
    val playerMVI: CommonMVI<PlayerActionState, PlayerNavigationEvent> =
        object : CommonMVI<PlayerActionState, PlayerNavigationEvent>() {
            override fun handleAction(action: PlayerActionState) {}
            override fun handleNavigationEvent(singleEvent: PlayerNavigationEvent) {}
        }

    BlueTubeComposeTheme {
        Surface {
            PlayerScreen(
                video = PREVIEW_VIDEO_LIST.first(),
                playerStateFlow = playerStateFlowLandscape,
                playerMVI = playerMVI,
                playbackPosition = 0F,
                isPreview = true
            )
        }
    }
}
