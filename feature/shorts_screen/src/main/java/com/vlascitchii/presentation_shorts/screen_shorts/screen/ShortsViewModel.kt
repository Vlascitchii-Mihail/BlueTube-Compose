package com.vlascitchii.presentation_shorts.screen_shorts.screen

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.vlascitchii.domain.di_common.SHORTS_CONVERTER
import com.vlascitchii.domain.di_common.SHORTS_USE_CASE
import com.vlascitchii.domain.model.videos.YoutubeVideoDomain
import com.vlascitchii.domain.usecase.ShortsUseCase
import com.vlascitchii.domain.usecase.UseCase
import com.vlascitchii.domain.util.VideoResult
import com.vlascitchii.presentation_common.model.util.CommonResultConverter
import com.vlascitchii.presentation_common.model.videos.YoutubeVideoUiModel
import com.vlascitchii.presentation_common.network_observer.NetworkConnectivityAbstraction
import com.vlascitchii.presentation_common.ui.screen.mvi.CommonMVIViewModel
import com.vlascitchii.presentation_common.ui.state_common.UiState
import com.vlascitchii.presentation_shorts.screen_shorts.state.ShortsAction
import com.vlascitchii.presentation_shorts.screen_shorts.state.ShortsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
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
    private val dispatcher: CoroutineDispatcher
) : CommonMVIViewModel<ShortsAction>() {

    val videoQueueSize: Int = 3
    val videoQueue: MutableSharedFlow<YouTubePlayer?> = MutableSharedFlow(videoQueueSize)

    private var _shortsStateFlow: MutableStateFlow<UiState<Flow<PagingData<YoutubeVideoUiModel>>>> =
        MutableStateFlow<UiState<Flow<PagingData<YoutubeVideoUiModel>>>>(UiState.Loading)

    private val _shortsUiStateFlow: StateFlow<ShortsUiState> = combine(
        flow = _shortsStateFlow,
        flow2 = networkConnectivityObserver.observe()
    ) { shortsStateFlow, networkConnectivityObserver ->
        ShortsUiState(
            shortsState = shortsStateFlow,
            networkConnectivityStatus = networkConnectivityObserver
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = ShortsUiState()

    )
    val shortsUiStateFlow: StateFlow<ShortsUiState> = _shortsUiStateFlow

    override fun handleAction(action: ShortsAction) {
        when(action) {
            is ShortsAction.ListenToVideoQueueAction -> listenToVideoQueue()
            is ShortsAction.FetchShortsAction -> fetchShorts()
        }
    }

    fun fetchShorts() {
        viewModelScope.launch {
            shortsUseCase.execute(ShortsUseCase.ShortsRequest)
                .map { shortsVideoResult: VideoResult<ShortsUseCase.ShortsResponse> ->
                    cacheShorts(shortsVideoResult)
                }
                .flowOn(dispatcher)
                .collect { uiState: UiState<Flow<PagingData<YoutubeVideoUiModel>>> ->
                    _shortsStateFlow.value = uiState
                }
        }
    }

    private fun cacheShorts(shortsResult: VideoResult<ShortsUseCase.ShortsResponse>)
    : UiState<@JvmSuppressWildcards Flow<PagingData<YoutubeVideoUiModel>>> {
        val shortsFlow: Flow<PagingData<YoutubeVideoDomain>>? =
            shortsConverter.unpack(shortsResult)?.cachedIn(viewModelScope)
        return if (shortsFlow != null) shortsConverter.convertSuccessVideo(shortsFlow)
        else shortsConverter.convert(shortsResult)
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
