package com.vlascitchii.data_remote.source

import com.vlascitchii.data_remote.enetity_api_model.util.convertToYoutubeVideoList
import com.vlascitchii.data_remote.enetity_api_model.video_search_api_model.SearchVideoResponseApiModel
import com.vlascitchii.data_remote.networking.service.ParticularVideoApiService
import com.vlascitchii.data_remote.networking.service.ShortsApiService
import com.vlascitchii.data_remote.source.util_video_converter.RemoteConverter
import com.vlascitchii.data_repository.data_source.remote.RemoteShortsDataSource
import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideoResponse
import com.vlascitchii.domain.util.UseCaseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RemoteShortsDataSourceImpl @Inject constructor(
    private val shortsApiService: ShortsApiService
) : RemoteShortsDataSource, RemoteBaseDataSource<SearchVideoResponseApiModel>(shortsApiService), RemoteConverter {

    override val particularVideoApi: ParticularVideoApiService = shortsApiService

    override fun fetchShorts(nextPageToken: String): Flow<YoutubeVideoResponse> = flow {
        val retrofitResponse = shortsApiService.fetchShorts(nextPageToken = nextPageToken)
        val shortsPage: SearchVideoResponseApiModel = getDataOnSuccessOrThrowHttpExceptionOnError(retrofitResponse)

        val shortsVideoPage = shortsPage.items.convertToApiVideosList()
        shortsVideoPage.fillChannelUrl()

        emit(
            YoutubeVideoResponse(
                nextPageToken = shortsPage.nextPageToken,
                prevPageToken = shortsPage.prevPageToken,
                items = shortsVideoPage.convertToYoutubeVideoList()
            )
        )
    }.catch {
        throw UseCaseException.ShortsLoadException(it)
    }

    override fun checkResponseBodyItemsIsNoteEmpty(responseBody: SearchVideoResponseApiModel): Boolean {
        return responseBody.items.isNotEmpty()
    }
}