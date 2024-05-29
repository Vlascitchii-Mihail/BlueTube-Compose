package com.appelier.bluetubecompose.screen_shorts.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.appelier.bluetubecompose.screen_shorts.repository.ShortsRepository
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideo
import com.appelier.bluetubecompose.utils.VideoType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ShortsViewModel @Inject constructor(
    private val shortsRepository: ShortsRepository
): ViewModel() {

    fun getShorts(): StateFlow<PagingData<YoutubeVideo>> {
        return shortsRepository.fetchShorts(VideoType.Shorts, viewModelScope)
            .cachedIn(viewModelScope)
            .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())
    }
}