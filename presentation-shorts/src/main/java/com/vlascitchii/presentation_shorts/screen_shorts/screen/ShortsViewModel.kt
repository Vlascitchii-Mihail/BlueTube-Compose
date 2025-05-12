package com.vlascitchii.presentation_shorts.screen_shorts.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.vlascitchii.domain.usecase.ShortsUseCase
import com.vlascitchii.presentation_common.entity.videos.YoutubeVideoUiModel
import com.appelier.bluetubecompose.network_observer.NetworkConnectivityObserver
import com.vlascitchii.presentation_common.ui.state.UiState
import com.vlascitchii.presentation_shorts.screen_shorts.utils.ShortsConverter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShortsViewModel @Inject constructor(
    private val shortsUseCase: ShortsUseCase,
    private val shortsConverter: ShortsConverter,
    private val networkConnectivityObserver: com.appelier.bluetubecompose.network_observer.NetworkConnectivityObserver
): ViewModel() {

    val videoQueue: MutableSharedFlow<YouTubePlayer?> = MutableSharedFlow(3)
    private val initPageToken = ""

//    private var _shortsStateFlow: StateFlow<PagingData<YoutubeVideoUiModel>> = getShorts()
    private var _shortsStateFlow: MutableStateFlow<UiState<StateFlow<PagingData<YoutubeVideoUiModel>>>> =
    MutableStateFlow<UiState<StateFlow<PagingData<YoutubeVideoUiModel>>>>(UiState.Loading)
    val shortsStateFlow: StateFlow<UiState<StateFlow<PagingData<YoutubeVideoUiModel>>>> get() = _shortsStateFlow

    private var _connectivityObserver: Flow<com.appelier.bluetubecompose.network_observer.ConnectivityStatus> = networkConnectivityObserver.observe()
    val connectivityObserver: Flow<com.appelier.bluetubecompose.network_observer.ConnectivityStatus> = _connectivityObserver

    private fun getShorts() {
        viewModelScope.launch {
            shortsUseCase.execute(ShortsUseCase.Request(initPageToken)).map {
                shortsConverter.convert(it)
            }.collect {
                _shortsStateFlow.value = it
            }
        }
    }

    fun listenToVideoQueue() {
        viewModelScope.launch {
            videoQueue.collect() {
                delay(1500)
                if (videoQueue.replayCache.size == 1) videoQueue.replayCache.first()?.play()
                else videoQueue.replayCache[1]?.play()
            }
        }
    }
}
