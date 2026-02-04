package com.vlascitchii.bluetubecompose.fake.usecase

import androidx.paging.PagingData
import com.vlascitchii.bluetubecompose.fake.data.DOMAIN_VIDEO_LIST_WITH_CHANNEL_IMG_URL
import com.vlascitchii.domain.model.videos.YoutubeVideoDomain
import com.vlascitchii.domain.usecase.ShortsUseCase
import com.vlascitchii.domain.usecase.UseCase
import com.vlascitchii.domain.usecase.VideoListUseCase
import com.vlascitchii.domain.usecase.VideoPlayerUseCase
import com.vlascitchii.domain.usecase.util.DispatcherConfiguration
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

private val pagingData: Flow<PagingData<YoutubeVideoDomain>> = flowOf(PagingData.from(DOMAIN_VIDEO_LIST_WITH_CHANNEL_IMG_URL))

private val videoListUseCaseResponse: Flow<VideoListUseCase.VideoListResponse> = flowOf(VideoListUseCase.VideoListResponse(pagingData))
private val videoPlayerUseCaseResponse: Flow<VideoPlayerUseCase.PlayerResponse> = flowOf(VideoPlayerUseCase.PlayerResponse(pagingData))
private val shortsUseCaseResponse: Flow<ShortsUseCase.ShortsResponse> = flowOf(ShortsUseCase.ShortsResponse(pagingData))

class FakeVideoListTestUseCase @Inject constructor(
    dispatcherConfiguration: DispatcherConfiguration
) : UseCase<VideoListUseCase.VideoListRequest, VideoListUseCase.VideoListResponse>(dispatcherConfiguration) {

    override fun process(request: VideoListUseCase.VideoListRequest): Flow<VideoListUseCase.VideoListResponse> = videoListUseCaseResponse
}

class FakeVideoPlayerUseCase @Inject constructor(
    dispatcherConfiguration: DispatcherConfiguration
) : UseCase<VideoPlayerUseCase.PlayerRequest, VideoPlayerUseCase.PlayerResponse>(dispatcherConfiguration) {

    override fun process(request: VideoPlayerUseCase.PlayerRequest): Flow<VideoPlayerUseCase.PlayerResponse> = videoPlayerUseCaseResponse
}

class FakeShortsUseCase @Inject constructor(
    configuration: DispatcherConfiguration,
) : UseCase<ShortsUseCase.ShortsRequest, ShortsUseCase.ShortsResponse>(configuration) {

    override fun process(request: ShortsUseCase.ShortsRequest): Flow<ShortsUseCase.ShortsResponse> = shortsUseCaseResponse
}
