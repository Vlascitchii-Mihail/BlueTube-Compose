package com.vlascitchii.presentation_player.screen_player.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.vlascitchii.domain.custom_scope.CustomCoroutineScope
import com.vlascitchii.domain.di_common.PLAYER_CONVERTER
import com.vlascitchii.domain.di_common.VIDEO_PLAYER_USE_CASE
import com.vlascitchii.domain.usecase.UseCase
import com.vlascitchii.domain.usecase.VideoPlayerUseCase
import com.vlascitchii.domain.util.VideoResult
import com.vlascitchii.presentation_common.model.util.CommonResultConverter
import com.vlascitchii.presentation_common.model.videos.YoutubeVideoUiModel
import com.vlascitchii.presentation_common.network_observer.NetworkConnectivityAbstraction
import com.vlascitchii.presentation_common.ui.state_common.UiState
import com.vlascitchii.presentation_common.utils.combineSix
import com.vlascitchii.presentation_player.screen_player.OrientationState
import com.vlascitchii.presentation_player.screen_player.state.PlayerState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

private const val INITIAL_VIDEO_PLAYBACK_POSITION = 0F

@HiltViewModel
class VideoPlayerViewModel @Inject constructor(
    @param:Named(VIDEO_PLAYER_USE_CASE)
    private val videoPlayerUseCase: UseCase<VideoPlayerUseCase.PlayerRequest, VideoPlayerUseCase.PlayerResponse>,
    @param:Named(PLAYER_CONVERTER)
    private val videoPlayerConverter: CommonResultConverter<VideoPlayerUseCase.PlayerResponse, @JvmSuppressWildcards Flow<PagingData<YoutubeVideoUiModel>>>,
    private val networkConnectivityObserver: NetworkConnectivityAbstraction,
    private val customCoroutineScope: CustomCoroutineScope
) : ViewModel() {

    private val _relatedVideoStateFlow: MutableStateFlow<UiState<Flow<PagingData<YoutubeVideoUiModel>>>> =
        MutableStateFlow<UiState<Flow<PagingData<YoutubeVideoUiModel>>>>(UiState.Loading)
    var videoPlaybackPosition: Float = INITIAL_VIDEO_PLAYBACK_POSITION
        private set
    private val _isVideoPlayingFlow: MutableStateFlow<Boolean> = MutableStateFlow(true)
    private val _playerOrientationState: MutableStateFlow<OrientationState> =
        MutableStateFlow(OrientationState.PORTRAIT)
    private val _fullscreenWidgetIsClicked: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val _isOrientationApproved: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private var _playerStateFlow: StateFlow<PlayerState> = combineSix(
        flow1 = _relatedVideoStateFlow,
        flow2 = _isVideoPlayingFlow,
        flow3 = _playerOrientationState,
        flow4 = _fullscreenWidgetIsClicked,
        flow5 = networkConnectivityObserver.observe(),
        flow6 = _isOrientationApproved
    ) { relatedVideoState, isVideoPlaying, playerOrientationState, fullscreenWidgetIsClicked, networkConnectivityObserver, isOrientationApproved ->
        PlayerState(
            relatedVideoState = relatedVideoState,
            isVideoPlaying = isVideoPlaying,
            playerOrientationState = playerOrientationState,
            fullscreenWidgetIsClicked = fullscreenWidgetIsClicked,
            networkConnectivityStatus = networkConnectivityObserver,
            isOrientationApproved = isOrientationApproved
        )
    }.stateIn(
        viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = PlayerState()
    )
    val playerStateFlow: StateFlow<PlayerState> = _playerStateFlow

    fun updatePlaybackPosition(newPlaybackTime: Float) {
        videoPlaybackPosition = newPlaybackTime
    }

    fun getSearchedRelatedVideos(query: String) {
        viewModelScope.launch(customCoroutineScope.coroutineContext) {
            videoPlayerUseCase.execute(VideoPlayerUseCase.PlayerRequest(query, viewModelScope))
                .map { playerSearchVideoResult: VideoResult<VideoPlayerUseCase.PlayerResponse> ->
                    videoPlayerConverter.convert(playerSearchVideoResult)
                }.collect { uiState: UiState<Flow<PagingData<YoutubeVideoUiModel>>> ->
                    _relatedVideoStateFlow.value = uiState
                }
        }
    }

    fun updateVideoPlayState(isPlaying: Boolean) {
        _isVideoPlayingFlow.value = isPlaying
    }

    fun updatePlayerOrientationState(newPlayerOrientationState: OrientationState) {
        _playerOrientationState.value = newPlayerOrientationState
    }

    fun setInitialScreenLaunchRotationController(isFirstTimeScreenOpened: Boolean) {
        _isOrientationApproved.value = isFirstTimeScreenOpened
    }
}
