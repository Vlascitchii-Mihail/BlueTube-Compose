package com.vlascitchii.data_remote.source

import com.vlascitchii.data_remote.model_api.search_api_model.SearchVideoResponseApiModel
import com.vlascitchii.data_remote.model_api.video_api_model.convertToDomainYouTubeVideoResponse
import com.vlascitchii.data_remote.networking.service.ShortsApiService
import com.vlascitchii.data_remote.source.util.MoshiParser
import com.vlascitchii.data_repository.data_source.remote.RemoteVideoListDataSource
import com.vlascitchii.domain.model.videos.YoutubeVideoResponseDomain
import javax.inject.Inject

class RemoteShortsDataSourceImpl @Inject constructor(
    private val shortsApiService: ShortsApiService,
    moshiParser: MoshiParser
) : RemoteVideoListDataSource,
    RemoteBaseVideoDataSource<SearchVideoResponseApiModel>(
        baseApiService = shortsApiService,
        moshiParser = moshiParser
    ) {

    override suspend fun fetchVideos(nextPageToken: String): YoutubeVideoResponseDomain {
        return fetch(fetchData = { shortsApiService.fetchShorts(nextPageToken = nextPageToken) } )
    }

    override fun checkResponseBodyItemsIsNoteEmpty(responseBody: SearchVideoResponseApiModel): Boolean {
        return responseBody.items.isNotEmpty()
    }

    override suspend fun returnHandledVideoResult(successResponse: SearchVideoResponseApiModel): YoutubeVideoResponseDomain {
        val videoResponseApiModel = successResponse.convertToVideoResponseApiModel()
        return fillChannelUrlFields(videoResponseApiModel).convertToDomainYouTubeVideoResponse()
    }
}
