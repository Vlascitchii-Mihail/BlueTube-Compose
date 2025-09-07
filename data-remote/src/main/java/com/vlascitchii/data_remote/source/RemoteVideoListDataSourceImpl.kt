package com.vlascitchii.data_remote.source

import com.vlascitchii.data_remote.model_api.video_api_model.YoutubeVideoResponseApiModel
import com.vlascitchii.data_remote.model_api.video_api_model.convertToDomainYouTubeVideoResponse
import com.vlascitchii.data_remote.networking.service.VideoListApiService
import com.vlascitchii.data_remote.source.util.MoshiParser
import com.vlascitchii.data_repository.data_source.remote.RemoteVideoListDataSource
import com.vlascitchii.domain.model.videos.YoutubeVideoResponseDomain
import javax.inject.Inject

class RemoteVideoListDataSourceImpl @Inject constructor(
    private val videoListApiService: VideoListApiService,
    moshiParser: MoshiParser,
) : RemoteVideoListDataSource, RemoteBaseVideoDataSource<YoutubeVideoResponseApiModel>(
    baseApiService = videoListApiService,
    moshiParser = moshiParser
) {

    override suspend fun fetchVideos(nextPageToken: String): YoutubeVideoResponseDomain {
        return fetch(fetchData = { videoListApiService.fetchVideos(nextPageToken = nextPageToken) })
    }

    override fun checkResponseBodyItemsIsNoteEmpty(responseBody: YoutubeVideoResponseApiModel): Boolean {
        return responseBody.items.isNotEmpty()
    }

    override suspend fun returnHandledVideoResult(successResponse: YoutubeVideoResponseApiModel): YoutubeVideoResponseDomain {
        return fillChannelUrlFields(successResponse).convertToDomainYouTubeVideoResponse()
    }
}
