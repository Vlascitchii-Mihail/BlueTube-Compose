package com.appelier.bluetubecompose.core.core_paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.appelier.bluetubecompose.core.core_api.VideoApiService
import com.appelier.bluetubecompose.utils.VideoType
import com.appelier.bluetubecompose.search_video.model.SearchVideoItem
import com.appelier.bluetubecompose.search_video.model.SearchVideoResponse
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideo
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideoResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class YoutubeVideoSource(
    private val apiService: VideoApiService,
    private val viewModelScope: CoroutineScope,
    private val videoType: VideoType
): PagingSource<String, YoutubeVideo>() {

    override suspend fun load(params: LoadParams<String>): LoadResult<String, YoutubeVideo> {
        return try {
            val nextPageToken = params.key ?: ""
            val videos = getLoadData(nextPageToken, videoType)

            LoadResult.Page(
                data = videos.items,
                prevKey = videos.prevPageToken,
                nextKey = videos.nextPageToken
            )
        } catch (ex: IOException) {
            return LoadResult.Error(ex)
        } catch (ex: HttpException) {
            return LoadResult.Error(ex)
        } catch (ex: Exception) {
            return LoadResult.Error(ex)
        }
    }

    suspend fun getLoadData(nextPageToken: String, videoType: VideoType): YoutubeVideoResponse {
        return when(videoType) {
            is VideoType.Videos -> fetchVideos(nextPageToken)
            is VideoType.SearchedVideo -> fetchSearchedVideos(videoType.query, nextPageToken)
            is VideoType.Shorts -> fetchShorts(nextPageToken)
            is VideoType.SearchedRelatedVideo -> fetchSearchedRelatedVideos(videoType.query, nextPageToken)
        }
    }

    private suspend fun fetchVideos(nextPageToken: String): YoutubeVideoResponse {
        val videos = apiService.fetchVideos(nextPageToken = nextPageToken).body()!!
        videos.items.addChannelImgUrl()
        return videos
    }

    private suspend fun fetchSearchedVideos(query: String, nextPageToken: String)
    : YoutubeVideoResponse {
        val searchedVideos = apiService.searchVideo(query, nextPageToken = nextPageToken).body()!!
        val videos: List<YoutubeVideo> = searchedVideos.items.convertToVideosList()
        videos.addChannelImgUrl()
        return YoutubeVideoResponse(
            searchedVideos.nextPageToken,
            searchedVideos.prevPageToken,
            items = videos
        )
    }

    private suspend fun fetchSearchedRelatedVideos(query: String, nextPageToken: String)
            : YoutubeVideoResponse {
        val searchedVideos = apiService.searchVideo(query, nextPageToken = nextPageToken).body()!!
        val newSearchedVideos = searchedVideos.deleteFirstSameVideo()
        val videos: List<YoutubeVideo> = newSearchedVideos.items.convertToVideosList()
        videos.addChannelImgUrl()
        return YoutubeVideoResponse(
            newSearchedVideos.nextPageToken,
            newSearchedVideos.prevPageToken,
            items = videos
        )
    }

    private suspend fun fetchShorts(nextPageToken: String): YoutubeVideoResponse {
        val shorts = apiService.fetchShorts(nextPageToken = nextPageToken).body()!!
        val shortsVideos = shorts.items.convertToVideosList()
        shortsVideos.addChannelImgUrl()
        return YoutubeVideoResponse(shorts.nextPageToken, shorts.prevPageToken, items = shortsVideos)
    }

    private fun SearchVideoResponse.deleteFirstSameVideo(): SearchVideoResponse {
        val mutableVideoList = this.items.toMutableList()
        mutableVideoList.removeAt(0)
        return this.copy(items = mutableVideoList)
    }

    private suspend fun  List<SearchVideoItem>.convertToVideosList(): List<YoutubeVideo> {
        val videoList: MutableList<Deferred<YoutubeVideo>> = mutableListOf()
        coroutineScope {
            this@convertToVideosList.forEach { searchedVideo ->
                val video = async { searchedVideo.convertToVideo() }
                videoList.add(video)
            }
        }
        return videoList.awaitAll()
    }

    suspend fun  List<YoutubeVideo>?.addChannelImgUrl() {
        this?.let {
            it.addUrl(getChannelImgUrl(it))
        }
    }

    private fun List<YoutubeVideo>.addUrl(
        channelUrlList: List<String>
    ) {
        for (i in this.indices) {
            this[i].snippet.channelImgUrl = channelUrlList[i]
        }
    }

    private suspend fun SearchVideoItem.convertToVideo(): YoutubeVideo {
        val video = apiService.fetchParticularVideo(this.id.videoId).body()!!
        return video.items.first()
    }

    private suspend fun getChannelImgUrl(videos: List<YoutubeVideo>): List<String> {
        val channelUrlList: MutableList<Deferred<String>> = mutableListOf()
        coroutineScope {
            videos.map { video: YoutubeVideo ->
                val channelImgUrl = async {
                    val channelResponse = apiService.fetchChannels(video.snippet.channelId).body()!!
                    channelResponse.items.first().snippet.thumbnails.medium.url
                }
                channelUrlList.add(channelImgUrl)
            }
        }
        return channelUrlList.awaitAll()
    }

    override fun getRefreshKey(state: PagingState<String, YoutubeVideo>): String? {
        var current: String? = " "
        val anchorPosition = state.anchorPosition

        viewModelScope.launch {
            if (anchorPosition != null) {
                current = state.closestPageToPosition(anchorPosition)?.prevKey?.let {
                    apiService.fetchVideos(nextPageToken = it).body()?.nextPageToken
                }
            }
        }
        return current
    }
}