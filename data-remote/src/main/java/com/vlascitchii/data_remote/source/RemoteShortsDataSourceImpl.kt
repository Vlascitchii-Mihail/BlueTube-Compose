package com.vlascitchii.data_remote.source

import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideoResponse
import com.vlascitchii.data_remote.networking.service.ParticularVideoApiService
import com.vlascitchii.data_remote.networking.service.ShortsApiService
import com.vlascitchii.data_remote.source.util_video_converter.RemoteConverter
import com.vlascitchii.data_remote.enetity_api_model.util.convertToYoutubeVideoList
import com.vlascitchii.data_repository.data_source.remote.RemoteShortsDataSource
import com.vlascitchii.domain.custom_coroutine_scopes.AppCoroutineScope
import com.vlascitchii.domain.util.UseCaseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Named

class RemoteShortsDataSourceImpl @Inject constructor(
    private val shortsApiService: ShortsApiService,
    @Named("video")
    private val videoCoroutineScope: AppCoroutineScope
) : RemoteShortsDataSource, RemoteBaseDataSource(shortsApiService), RemoteConverter {

    override val particularVideoApi: ParticularVideoApiService = shortsApiService
    override val remoteConverterDataScope: AppCoroutineScope = videoCoroutineScope

    override fun fetchShorts(nextPageToken: String): Flow<YoutubeVideoResponse> = flow {
        val shortsVideoResponse = shortsApiService.fetchShorts(nextPageToken =  nextPageToken).body()

        if (shortsVideoResponse != null && shortsVideoResponse.items.isNotEmpty()) {
            val shortsVideoList = shortsVideoResponse.items.convertToVideosList()

            shortsVideoList.fillChannelUrl()

            emit(
                YoutubeVideoResponse(
                    nextPageToken = shortsVideoResponse.nextPageToken,
                    prevPageToken = shortsVideoResponse.prevPageToken,
                    items = shortsVideoList.convertToYoutubeVideoList()
                )
            )
        }
    }.catch {
        throw UseCaseException.ShortsLoadException(it)
    }.flowOn(videoCoroutineScope.dispatcher)
}