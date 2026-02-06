package com.vlascitchii.presentation_shorts.screen_shorts.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.vlascitchii.domain.di_common.SHORTS_CONVERTER
import com.vlascitchii.domain.di_common.SHORTS_USE_CASE
import com.vlascitchii.domain.usecase.ShortsUseCase
import com.vlascitchii.domain.usecase.UseCase
import com.vlascitchii.domain.util.VideoResult
import com.vlascitchii.presentation_common.model.util.CommonResultConverter
import com.vlascitchii.presentation_common.model.videos.YoutubeVideoUiModel
import com.vlascitchii.presentation_common.network_observer.NetworkConnectivityAbstraction
import com.vlascitchii.presentation_common.network_observer.NetworkConnectivityStatus
import com.vlascitchii.presentation_common.ui.state_common.UiState
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
    @param:Named(SHORTS_USE_CASE)
    private val shortsUseCase: UseCase<ShortsUseCase.ShortsRequest, ShortsUseCase.ShortsResponse>,
    @param:Named(SHORTS_CONVERTER)
    private val shortsConverter: CommonResultConverter<ShortsUseCase.ShortsResponse, @JvmSuppressWildcards Flow<PagingData<YoutubeVideoUiModel>>>,
    private val networkConnectivityObserver: NetworkConnectivityAbstraction,
) : ViewModel() {

    val videoQueueSize: Int = 3
    val videoQueue: MutableSharedFlow<YouTubePlayer?> = MutableSharedFlow(videoQueueSize)

    private var _shortsStateFlow: MutableStateFlow<UiState<Flow<PagingData<YoutubeVideoUiModel>>>> =
        MutableStateFlow<UiState<Flow<PagingData<YoutubeVideoUiModel>>>>(UiState.Loading)
    val shortsStateFlow: StateFlow<UiState<Flow<PagingData<YoutubeVideoUiModel>>>> get() = _shortsStateFlow

    private var _connectivityObserver: Flow<NetworkConnectivityStatus> =
        networkConnectivityObserver.observe()
    val connectivityObserver: Flow<NetworkConnectivityStatus> = _connectivityObserver

    init {
        fetchShorts()
    }

    fun fetchShorts() {
        viewModelScope.launch {
            shortsUseCase.execute(ShortsUseCase.ShortsRequest(viewModelScope))
                .map { shortsVideoResult: VideoResult<ShortsUseCase.ShortsResponse> ->
                    shortsConverter.convert(shortsVideoResult)
                }.collect { uiState: UiState<Flow<PagingData<YoutubeVideoUiModel>>> ->
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
}
