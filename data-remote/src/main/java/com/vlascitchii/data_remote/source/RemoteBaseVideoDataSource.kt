package com.vlascitchii.data_remote.source

import com.vlascitchii.data_remote.model_api.channel_api_model.ChannelApiModel
import com.vlascitchii.data_remote.model_api.channel_api_model.YoutubeChannelResponseApiModel
import com.vlascitchii.data_remote.model_api.error.convertErrorApiYouTubeResponseToErrorDomainYouTubeResponse
import com.vlascitchii.data_remote.model_api.search_api_model.SearchVideoItemApiModel
import com.vlascitchii.data_remote.model_api.search_api_model.SearchVideoResponseApiModel
import com.vlascitchii.data_remote.model_api.video_api_model.YoutubeVideoApiModel
import com.vlascitchii.data_remote.model_api.video_api_model.YoutubeVideoResponseApiModel
import com.vlascitchii.data_remote.networking.service.BaseApiService
import com.vlascitchii.data_remote.source.util.MoshiParser
import com.vlascitchii.domain.model.ErrorDomain
import com.vlascitchii.domain.model.videos.YoutubeVideoResponseDomain
import com.vlascitchii.domain.util.UseCaseException
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response

abstract class RemoteBaseVideoDataSource<T>(
    private val baseApiService: BaseApiService,
    private val moshiParser: MoshiParser
) {

    private var errorBody: ResponseBody? = null

    suspend fun fillChannelUrlFields(videoResponseNoImageUrl: YoutubeVideoResponseApiModel): YoutubeVideoResponseApiModel {
        val channelUrlList = getChannelImgUrlList(videoResponseNoImageUrl.items)
        return videoResponseNoImageUrl.addChannelUrl(channelUrlList)
    }

    suspend fun getChannelImgUrlList(videos: List<YoutubeVideoApiModel>): List<String> {
        val channelsIdList: List<String> = videos.map { video: YoutubeVideoApiModel ->
            video.snippet.channelId
        }
        val videoListResponse: Response<YoutubeChannelResponseApiModel> =
            baseApiService.fetchChannels(idList = channelsIdList)

        val channelIdList: List<String> =
            videoListResponse.body()?.items?.map { channel: ChannelApiModel ->
                channel.snippet.thumbnails.medium.url
            } ?: emptyList()
        return channelIdList
    }

    fun YoutubeVideoResponseApiModel.addChannelUrl(channelUrlList: List<String>): YoutubeVideoResponseApiModel {
        val newItems = this.items.mapIndexed { index: Int, videoItem: YoutubeVideoApiModel ->
            val newVideoSnippet = videoItem.snippet.copy(
                channelImgUrl = getElementIfExists(
                    index = index,
                    channelUrlList = channelUrlList
                )
            )
            videoItem.copy(snippet = newVideoSnippet)
        }
        return this.copy(items = newItems)
    }

    private fun getElementIfExists(index: Int, channelUrlList: List<String>): String {
        val listSize = channelUrlList.size
        return if (index > (listSize - 1)) "" else channelUrlList[index]
    }

    suspend fun SearchVideoResponseApiModel.convertToVideoResponseApiModel(): YoutubeVideoResponseApiModel {
        val searchedVideoIdList: List<String> =
            this.items.map { searchedVideo: SearchVideoItemApiModel ->
                searchedVideo.id.videoId
            }
        val videoListResponse: Response<YoutubeVideoResponseApiModel> =
            baseApiService.fetchParticularVideoList(idList = searchedVideoIdList)

        val videoList = videoListResponse.body()
            ?.copy(nextPageToken = this.nextPageToken, prevPageToken = this.prevPageToken)
            ?: YoutubeVideoResponseApiModel()

        return videoList
    }

    abstract fun checkResponseBodyItemsIsNoteEmpty(responseBody: T): Boolean

    fun getDataOnSuccessOrThrowHttpExceptionOnError(response: Response<T>): T {
        val responseBody: T? = response.body()
        return when (true) {
            (response.isSuccessful && responseBody != null && checkResponseBodyItemsIsNoteEmpty(
                responseBody
            )) -> responseBody

            else -> {
                errorBody = response.errorBody()
                throw HttpException(response)
            }
        }
    }

    fun getYouTubeDomainErrorFromErrorBody(): ErrorDomain? {
        return moshiParser.parseJsonIntoNewsApiErrorResponse(errorBody?.string() ?: "")
            ?.convertErrorApiYouTubeResponseToErrorDomainYouTubeResponse()
    }

    suspend fun fetch(fetchData: suspend () -> Response<T>): YoutubeVideoResponseDomain {
        return try {
            val apiResponse: Response<T> = fetchData.invoke()
            val successResponse: T = getDataOnSuccessOrThrowHttpExceptionOnError(apiResponse)

            returnHandledVideoResult(successResponse)
        } catch (exception: Exception) {
            exception.printStackTrace()
            val youTubeErrorResponse: ErrorDomain? = getYouTubeDomainErrorFromErrorBody()
            throw UseCaseException.VideoListLoadException(exception, youTubeErrorResponse)
        }
    }

    abstract suspend fun returnHandledVideoResult(successResponse: T): YoutubeVideoResponseDomain
}
