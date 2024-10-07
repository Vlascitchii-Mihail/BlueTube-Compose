package com.appelier.bluetubecompose.screen_shorts.screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.appelier.bluetubecompose.screen_shorts.repository.ShortsRepository
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideo
import com.appelier.bluetubecompose.utils.VideoType
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShortsViewModel @Inject constructor(
    private val shortsRepository: ShortsRepository
): ViewModel() {
    val videoQueue: MutableSharedFlow<YouTubePlayer?> = MutableSharedFlow(3)

    fun listenToVideoQueue() {
        viewModelScope.launch {
            videoQueue.collect() {
                delay(1500)
                if (videoQueue.replayCache.size == 1) videoQueue.replayCache.first()?.play()
                else videoQueue.replayCache[1]?.play()
            }
        }
    }

    private val emptyPagingData = PagingData.empty<YoutubeVideo>()
    private var _shortsVideoState: MutableState<StateFlow<PagingData<YoutubeVideo>>> = mutableStateOf(
        MutableStateFlow(emptyPagingData))
    private val shortsVideoState: State<StateFlow<PagingData<YoutubeVideo>>> get() = _shortsVideoState

    fun getShortsVideo() = shortsVideoState

    fun getShorts() {
        val youTubeStateFlow = shortsRepository.fetchShorts(VideoType.Shorts, viewModelScope)
            .cachedIn(viewModelScope)
            .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())

        _shortsVideoState = mutableStateOf(youTubeStateFlow)
    }
}
