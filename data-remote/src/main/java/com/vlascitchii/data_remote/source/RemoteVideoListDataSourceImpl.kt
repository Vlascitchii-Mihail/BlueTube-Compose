package com.vlascitchii.data_remote.source

import com.vlascitchii.data_remote.enetity_api_model.util.convertToYouTubeVideoResponse
import com.vlascitchii.data_remote.enetity_api_model.video_list_api_model.videos_api_model.YoutubeVideoResponseApiModel
import com.vlascitchii.data_remote.networking.service.VideoListApiService
import com.vlascitchii.data_repository.data_source.remote.RemoteVideoListDataSource
import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideoResponse
import com.vlascitchii.domain.util.UseCaseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RemoteVideoListDataSourceImpl @Inject constructor(
    private val videoListChannelApiService: VideoListApiService
) : RemoteVideoListDataSource, RemoteBaseDataSource<YoutubeVideoResponseApiModel>(videoListChannelApiService) {

    override fun fetchVideos(nextPageToken: String): Flow<YoutubeVideoResponse> = flow {

        val retrofitResponse = videoListChannelApiService.fetchVideos(nextPageToken = nextPageToken)
        val videoPage: YoutubeVideoResponseApiModel = getDataOnSuccessOrThrowHttpExceptionOnError(retrofitResponse)

        videoPage.items.fillChannelUrl()
        emit(videoPage.convertToYouTubeVideoResponse())

    }.catch {
        throw UseCaseException.VideoListLoadException(it)
    }

    override fun checkResponseBodyItemsIsNoteEmpty(responseBody: YoutubeVideoResponseApiModel): Boolean {
        return responseBody.items.isNotEmpty()
    }
}