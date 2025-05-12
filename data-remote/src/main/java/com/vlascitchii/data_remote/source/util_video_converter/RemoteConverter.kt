package com.vlascitchii.data_remote.source.util_video_converter

import com.vlascitchii.data_remote.enetity_api_model.video_list_api_model.videos_api_model.YoutubeVideoApiModel
import com.vlascitchii.data_remote.enetity_api_model.video_search_api_model.SearchVideoItemApiModel
import com.vlascitchii.data_remote.networking.service.ParticularVideoApiService
import com.vlascitchii.domain.custom_coroutine_scopes.AppCoroutineScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

interface RemoteConverter {

    val particularVideoApi: ParticularVideoApiService
    val remoteConverterDataScope: AppCoroutineScope

    //TODO: try to migrate to the kotlin flows
    suspend fun List<SearchVideoItemApiModel>.convertToVideosList(): List<YoutubeVideoApiModel> =
        coroutineScope {
            this@convertToVideosList.map { searchedVideoApiModel ->
                async {
                    searchedVideoApiModel.convertToVideo()
                }
            }.awaitAll().filterNotNull()
        }


    //TODO: try to migrate to the kotlin flows
    suspend fun SearchVideoItemApiModel.convertToVideo(): YoutubeVideoApiModel? {

        var youtubeVideoApiModel: YoutubeVideoApiModel? = null
        try {
            val particularVideo = particularVideoApi.fetchParticularVideo(this.id.videoId).body()
            youtubeVideoApiModel = particularVideo?.items?.first()

            //TODO: correct the exception catcher
        } catch (ex: NoSuchElementException) {
            ex.printStackTrace()
        }
        ///TODO: try to get rid of nullability
        return youtubeVideoApiModel
    }
}