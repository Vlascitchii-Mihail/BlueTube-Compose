package com.vlascitchii.data_remote.source

import com.vlascitchii.data_remote.model_api.search_api_model.SearchVideoResponseApiModel
import com.vlascitchii.data_remote.model_api.video_api_model.convertToDomainYouTubeVideoResponse
import com.vlascitchii.data_remote.networking.service.VideoListApiService
import com.vlascitchii.data_remote.source.util.MoshiParser
import com.vlascitchii.data_repository.data_source.remote.RemoteSearchDataSource
import com.vlascitchii.domain.model.videos.YoutubeVideoResponseDomain
import javax.inject.Inject

class RemoteSearchVideoDataSourceImpl @Inject constructor(
    private val videoListApiService: VideoListApiService,
    moshiParser: MoshiParser
) : RemoteSearchDataSource, RemoteBaseVideoDataSource<SearchVideoResponseApiModel>(
    baseApiService = videoListApiService,
    moshiParser = moshiParser
) {

    override var isRelated: Boolean = false

    override suspend fun searchRelatedVideos(query: String, nextPageToken: String): YoutubeVideoResponseDomain {
        isRelated = true
        return search(query, nextPageToken)
    }

    override suspend fun searchVideos(query: String, nextPageToken: String): YoutubeVideoResponseDomain {
        isRelated = false
        return search(query, nextPageToken)
    }

    private suspend fun search(query: String, nextPageToken: String): YoutubeVideoResponseDomain {
        return fetch(fetchData = { videoListApiService.searchVideo(query = query, nextPageToken = nextPageToken)})
    }

    fun SearchVideoResponseApiModel.deleteFirstSameVideo(): SearchVideoResponseApiModel {
        val mutableVideoList = this.items.toMutableList()
        val firstElementIndex = 0
        mutableVideoList.removeAt(firstElementIndex)
        return this.copy(items = mutableVideoList)
    }

    override fun checkResponseBodyItemsIsNoteEmpty(responseBody: SearchVideoResponseApiModel): Boolean {
        return responseBody.items.isNotEmpty()
    }

    override suspend fun returnHandledVideoResult(successResponse: SearchVideoResponseApiModel): YoutubeVideoResponseDomain {
        val searchVideoPage = if (isRelated) successResponse.deleteFirstSameVideo() else successResponse
        val videoResponseApiModel = searchVideoPage.convertToVideoResponseApiModel()
        return fillChannelUrlFields(videoResponseApiModel).convertToDomainYouTubeVideoResponse()
    }
}
