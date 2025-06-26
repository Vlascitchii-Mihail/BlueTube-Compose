package com.vlascitchii.presentation_shorts.screen_shorts.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.vlascitchii.domain.custom_coroutine_scopes.AppCoroutineScope
import com.vlascitchii.domain.usecase.ShortsUseCase
import com.vlascitchii.domain.util.VideoResult
import com.vlascitchii.presentation_common.entity.videos.YoutubeVideoUiModel
import com.vlascitchii.presentation_common.network_observer.ConnectivityStatus
import com.vlascitchii.presentation_common.network_observer.NetworkConnectivityObserver
import com.vlascitchii.presentation_common.ui.state.UiState
import com.vlascitchii.presentation_shorts.screen_shorts.utils.ShortsConverter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class ShortsViewModel @Inject constructor(
    private val shortsUseCase: ShortsUseCase,
    private val shortsConverter: ShortsConverter,
    private val networkConnectivityObserver: NetworkConnectivityObserver,
    @Named("video") private val videoCoroutineScope: AppCoroutineScope,
) : ViewModel() {

    val videoQueueSize: Int = 3
    val videoQueue: MutableSharedFlow<YouTubePlayer?> = MutableSharedFlow(videoQueueSize)

    private var _shortsStateFlow: MutableStateFlow<UiState<PagingData<YoutubeVideoUiModel>>> =
        MutableStateFlow<UiState<PagingData<YoutubeVideoUiModel>>>(UiState.Loading)
    val shortsStateFlow: StateFlow<UiState<PagingData<YoutubeVideoUiModel>>> get() = _shortsStateFlow

    private var _connectivityObserver: Flow<ConnectivityStatus> =
        networkConnectivityObserver.observe()
    val connectivityObserver: Flow<ConnectivityStatus> = _connectivityObserver

    fun fetchShorts() {
        viewModelScope.launch {
            shortsUseCase.execute(ShortsUseCase.Request)
                .map { shortsVideoResult: VideoResult<ShortsUseCase.Response> ->
                    shortsConverter.convert(shortsVideoResult)
                }.collect { uiState: UiState<PagingData<YoutubeVideoUiModel>> ->
                    _shortsStateFlow.value = uiState
                }
        }
    }

    fun listenToVideoQueue() {
        viewModelScope.launch {
            videoQueue.collectLatest {
                delay(1500)
                if (videoQueue.replayCache.size == 1) videoQueue.replayCache.first()?.play()
                else videoQueue.replayCache[1]?.play()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        videoCoroutineScope.onStop()
    }
}
