package com.vlascitchii.data_remote.source

import com.vlascitchii.data_remote.enetity_api_model.video_list_api_model.videos_api_model.YoutubeVideoResponseApiModel
import com.vlascitchii.data_remote.networking.service.VideoListApiService
import com.vlascitchii.data_remote.enetity_api_model.util.convertToYouTubeVideoResponse
import com.vlascitchii.data_repository.data_source.remote.RemoteVideoListDataSource
import com.vlascitchii.domain.custom_coroutine_scopes.AppCoroutineScope
import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideoResponse
import com.vlascitchii.domain.util.UseCaseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Named

class RemoteVideoListDataSourceImpl @Inject constructor(
    private val videoListChannelApiService: VideoListApiService,
    @Named("video")
    private val videoCoroutineScope: AppCoroutineScope,
) : RemoteVideoListDataSource, RemoteBaseDataSource(videoListChannelApiService) {

    //listen to this flow in UI using repeatOnLifecycle()
    //or try to use flowWithLifecycle here (provide a lifecycle here if possible)
    override fun fetchVideos(nextPageToken: String): Flow<YoutubeVideoResponse> = flow {

        val videos: YoutubeVideoResponseApiModel? =
            videoListChannelApiService.fetchVideos(nextPageToken = nextPageToken).body()

        if (videos != null && videos.items.isNotEmpty()) {
            videos.items.fillChannelUrl()
            emit(videos.convertToYouTubeVideoResponse())
        }

    }.catch {
        throw UseCaseException.VideoListLoadException(it)
    }.flowOn(videoCoroutineScope.dispatcher)
}