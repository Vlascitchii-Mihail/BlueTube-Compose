package com.vlascitchii.data_remote.source

import com.vlascitchii.data_remote.enetity_api_model.video_list_api_model.videos_api_model.YoutubeVideoApiModel
import com.vlascitchii.data_remote.networking.service.BaseApiService
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

private const val EMPTY_URL = ""

abstract class RemoteBaseDataSource(private val baseApiService: BaseApiService) {

    suspend fun List<YoutubeVideoApiModel>.fillChannelUrl() {
        val channelUrlList = getChannelImgUrlList(this)
        this.addChannelUrl(channelUrlList)
    }

    //TODO: try to migrate to the kotlin flows
    suspend fun getChannelImgUrlList(videos: List<YoutubeVideoApiModel>): List<String> =
        coroutineScope {
            videos.map { video: YoutubeVideoApiModel ->
                async {
                    try {
                        val channelResponse =
                            baseApiService.fetchChannels(video.snippet.channelId)
                        channelResponse.body()?.items?.first()?.snippet?.thumbnails?.medium?.url ?: ""
                        //TODO: correct the exception catcher
                    } catch (ex: NoSuchElementException) {
                        ex.printStackTrace()
                        EMPTY_URL
                    }
                }
            }.awaitAll()
        }

    fun List<YoutubeVideoApiModel>.addChannelUrl(channelUrlList: List<String>) {
        for (i in this.indices) {
            this[i].snippet.channelImgUrl = channelUrlList[i]
        }
    }
}