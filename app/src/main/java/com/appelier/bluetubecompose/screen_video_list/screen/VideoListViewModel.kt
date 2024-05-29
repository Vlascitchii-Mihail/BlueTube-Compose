package com.appelier.bluetubecompose.screen_video_list.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideo
import com.appelier.bluetubecompose.screen_video_list.repository.VideoListRepository
import com.appelier.bluetubecompose.utils.VideoType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class VideoListViewModel @Inject constructor(
    private val videoRepository: VideoListRepository,
): ViewModel() {

    fun getVideosFlow(): StateFlow<PagingData<YoutubeVideo>> {
        return videoRepository
            .fetchVideos(VideoType.Videos, viewModelScope).cachedIn(viewModelScope)
            .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())
    }

    fun getSearchVideosFlow(query: String = ""): StateFlow<PagingData<YoutubeVideo>> {
        return videoRepository
            .fetchVideos(VideoType.SearchedVideo(query), viewModelScope)
            .cachedIn(viewModelScope)
            .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())
    }
}