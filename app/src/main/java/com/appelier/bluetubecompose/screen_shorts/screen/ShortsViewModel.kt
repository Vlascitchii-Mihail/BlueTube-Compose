package com.appelier.bluetubecompose.screen_shorts.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.appelier.bluetubecompose.core.core_api.network_observer.ConnectivityStatus
import com.appelier.bluetubecompose.core.core_api.network_observer.NetworkConnectivityObserver
import com.appelier.bluetubecompose.screen_shorts.repository.ShortsRepository
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideo
import com.appelier.bluetubecompose.utils.VideoType
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShortsViewModel @Inject constructor(
    private val shortsRepository: ShortsRepository,
    private val networkConnectivityObserver: NetworkConnectivityObserver
): ViewModel() {

    val videoQueue: MutableSharedFlow<YouTubePlayer?> = MutableSharedFlow(3)

    private var _shortsStateFlow: StateFlow<PagingData<YoutubeVideo>> = getShorts()
    val shortsStateFlow: StateFlow<PagingData<YoutubeVideo>> get() = _shortsStateFlow

    private var _connectivityObserver: Flow<ConnectivityStatus> = networkConnectivityObserver.observe()
    val connectivityObserver: Flow<ConnectivityStatus> = _connectivityObserver

    private fun getShorts(): StateFlow<PagingData<YoutubeVideo>> {
        return shortsRepository.fetchShorts(VideoType.Shorts, viewModelScope)
            .cachedIn(viewModelScope)
            .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())
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
