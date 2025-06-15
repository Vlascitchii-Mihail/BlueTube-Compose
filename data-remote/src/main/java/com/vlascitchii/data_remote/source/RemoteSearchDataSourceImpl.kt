package com.vlascitchii.data_remote.source

import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideoResponse
import com.vlascitchii.data_remote.networking.service.ParticularVideoApiService
import com.vlascitchii.data_remote.networking.service.SearchApiService
import com.vlascitchii.data_remote.source.util_video_converter.RemoteConverter
import com.vlascitchii.data_repository.data_source.remote.RemoteSearchDataSource
import com.vlascitchii.domain.custom_coroutine_scopes.AppCoroutineScope
import com.vlascitchii.data_remote.enetity_api_model.video_search_api_model.SearchVideoResponseApiModel
import com.vlascitchii.data_remote.enetity_api_model.util.convertToYoutubeVideoList
import com.vlascitchii.domain.util.UseCaseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Named

class RemoteSearchDataSourceImpl @Inject constructor(
    private val searchApiService: SearchApiService,
    @Named("video")
    private val videoCoroutineScope: AppCoroutineScope,
) : RemoteSearchDataSource, RemoteBaseDataSource<SearchVideoResponseApiModel>(searchApiService),
    RemoteConverter {

    override val particularVideoApi: ParticularVideoApiService = searchApiService
    override val remoteConverterDataScope: AppCoroutineScope = videoCoroutineScope
    override var isRelated: Boolean = false

    override fun searchRelatedVideos(
        query: String,
        nextPageToken: String
    ): Flow<YoutubeVideoResponse> {
        isRelated = true
        return search(query, nextPageToken)
    }

    override fun searchVideos(query: String, nextPageToken: String): Flow<YoutubeVideoResponse> {
        isRelated = false
        return search(query, nextPageToken)
    }

    override fun search(query: String, nextPageToken: String): Flow<YoutubeVideoResponse> =
        flow<YoutubeVideoResponse> {
            val retrofitResponse = searchApiService.searchVideo(query = query, nextPageToken = nextPageToken)
            var searchVideosPage: SearchVideoResponseApiModel = getDataOnSuccessOrThrowHttpExceptionOnError(retrofitResponse)

            if (isRelated) searchVideosPage = searchVideosPage.deleteFirstSameVideo()
            val videoList = searchVideosPage.items.convertToApiVideosList()
            videoList.fillChannelUrl()

            emit(
                YoutubeVideoResponse(
                    nextPageToken = searchVideosPage.nextPageToken,
                    prevPageToken = searchVideosPage.prevPageToken,
                    items = videoList.convertToYoutubeVideoList()
                )
            )

        }.catch {
            throw UseCaseException.SearchLoadException(it)
        }.flowOn(videoCoroutineScope.dispatcher)

    private fun SearchVideoResponseApiModel.deleteFirstSameVideo(): SearchVideoResponseApiModel {
        val mutableVideoList = this.items.toMutableList()
        mutableVideoList.removeAt(0)
        return this.copy(items = mutableVideoList)
    }

    override fun checkResponseBodyItemsIsNoteEmpty(responseBody: SearchVideoResponseApiModel): Boolean {
        return responseBody.items.isNotEmpty()
    }
}