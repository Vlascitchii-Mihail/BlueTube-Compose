package com.appelier.bluetubecompose.core.core_paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.appelier.bluetubecompose.core.core_api.VideoApiService
import com.appelier.bluetubecompose.core.core_database.YouTubeVideoDao
import com.appelier.bluetubecompose.core.core_database.utils.DatabaseContentManager
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideo
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideoResponse
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideoResponse.Companion.INITIAL_PAGE_TOKEN
import com.appelier.bluetubecompose.search_video.model.SearchVideoItem
import com.appelier.bluetubecompose.search_video.model.SearchVideoResponse
import com.appelier.bluetubecompose.utils.VideoType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

private const val DEFAULT_CHANNEL_IMG_LINK = "https://yt3.ggpht.com/ytc/AIdro_kQsQPf4J4FIufPC4XOJiWEdabmHjHFHvT_irLQXB0feS4=s88-c-k-c0x00ffffff-no-rj"

class YoutubeVideoSource(
    private val apiService: VideoApiService,
    private val viewModelScope: CoroutineScope,
    private val videoType: VideoType,
    private val youTubeVideoDao: YouTubeVideoDao
): PagingSource<String, YoutubeVideo>() {

    private var sourceCurrentPageToken = INITIAL_PAGE_TOKEN
    private val databaseContentManager = DatabaseContentManager(youTubeVideoDao)

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
            ex.printStackTrace()
            return LoadResult.Error(ex)
        } catch (ex: HttpException) {
            ex.printStackTrace()
            return LoadResult.Error(ex)
        } catch (ex: Exception) {
            ex.printStackTrace()
            return LoadResult.Error(ex)
        }
    }

    suspend fun getLoadData(nextPageToken: String, videoType: VideoType): YoutubeVideoResponse {
        val response = when(videoType) {
            is VideoType.Videos -> fetchVideos(nextPageToken)
            is VideoType.SearchedVideo -> fetchSearchedVideos(videoType.query, nextPageToken)
            is VideoType.Shorts -> fetchShorts(nextPageToken)
            is VideoType.SearchedRelatedVideo -> fetchSearchedRelatedVideos(videoType.query, nextPageToken)
        }

        return response
    }

    private suspend fun fetchVideos(nextPageToken: String): YoutubeVideoResponse {
        val videos = apiService.fetchVideos(nextPageToken = nextPageToken).body()

        return if (videos != null) {
            showVideosFromNetwork(videos)
        } else {
            showVideosFromDb(nextPageToken)
        }
    }

    private suspend fun showVideosFromNetwork(videoResponse:YoutubeVideoResponse): YoutubeVideoResponse {
        addChannelImgUrl(videoResponse.items)

        videoResponse.writeVideosToDatabase()

        return videoResponse
    }

    private suspend fun YoutubeVideoResponse.writeVideosToDatabase() {
        databaseContentManager.setCurrentPageTokenToVideos(
            sourceCurrentPageToken,
            this@writeVideosToDatabase
        )
        databaseContentManager.insertVideosToDatabaseWithTimeStamp(youTubeVideoResponse = this@writeVideosToDatabase)

        this.nextPageToken?.let { updateCurrentPageToken(it) }
    }

    private suspend fun showVideosFromDb(pageToken: String = ""): YoutubeVideoResponse {
        val dbVideos = databaseContentManager.getVideosFromDatabase(pageToken)
        dbVideos.nextPageToken?.let { updateCurrentPageToken(it) }
        return dbVideos
    }

    private suspend fun fetchSearchedVideos(query: String, nextPageToken: String)
            : YoutubeVideoResponse {
        val searchedVideos = apiService.searchVideo(query, nextPageToken = nextPageToken).body()

        return if (searchedVideos != null) {
            val videos: List<YoutubeVideo> = convertSearchVideoToVideosList(searchedVideos.items)
            val response = YoutubeVideoResponse(
                nextPageToken = searchedVideos.nextPageToken,
                prevPageToken = searchedVideos.prevPageToken,
                items = videos
            )
            showVideosFromNetwork(response)
        } else showVideosFromDb(nextPageToken)
    }

    private suspend fun fetchSearchedRelatedVideos(query: String, nextPageToken: String): YoutubeVideoResponse {
        val searchedVideos = apiService.searchVideo(query, nextPageToken = nextPageToken).body()

        return if (searchedVideos != null) {
            val newSearchedVideos = searchedVideos.deleteFirstSameVideo()
            val videos: List<YoutubeVideo> = convertSearchVideoToVideosList(newSearchedVideos.items)

            val response = YoutubeVideoResponse(
                nextPageToken = searchedVideos.nextPageToken,
                prevPageToken = searchedVideos.prevPageToken,
                items = videos
            )

            showVideosFromNetwork(response)
        } else showVideosFromDb(nextPageToken)
    }

    private suspend fun fetchShorts(nextPageToken: String): YoutubeVideoResponse {
        val shorts = apiService.fetchShorts(nextPageToken = nextPageToken).body()

        return if (shorts != null) {
            val shortsVideos = convertSearchVideoToVideosList(shorts.items)
            val response = YoutubeVideoResponse(
                nextPageToken = shorts.nextPageToken,
                prevPageToken = shorts.prevPageToken,
                items = shortsVideos
            )
            showVideosFromNetwork(response)
        } else {
            showVideosFromDb(nextPageToken)
        }
    }

    private fun SearchVideoResponse.deleteFirstSameVideo(): SearchVideoResponse {
        val mutableVideoList = this.items.toMutableList()
        mutableVideoList.removeAt(0)
        return this.copy(items = mutableVideoList)
    }

    suspend fun convertSearchVideoToVideosList(searchList: List<SearchVideoItem>): List<YoutubeVideo> {
        val videoList: MutableList<Deferred<YoutubeVideo>> = mutableListOf()
        coroutineScope {
            searchList.forEach { searchedVideo ->
                val video = async { searchedVideo.convertToVideo() }
                videoList.add(video)
            }
        }
        return videoList.awaitAll()
    }

    private suspend fun SearchVideoItem.convertToVideo(): YoutubeVideo {
        val video = apiService.fetchParticularVideo(this.id.videoId).body()

        return video?.items?.first() ?: YoutubeVideo.DEFAULT_VIDEO
    }

    suspend fun addChannelImgUrl(videoList: List<YoutubeVideo>) {
        val urlList = getChannelImgUrlList(videoList)
        videoList.addChannelUrl(urlList)
    }

    private fun List<YoutubeVideo>.addChannelUrl(channelUrlList: List<String>) {
        for (i in this.indices) {
            this[i].snippet.channelImgUrl = channelUrlList[i]
        }
    }

    private suspend fun getChannelImgUrlList(videos: List<YoutubeVideo>): List<String> {
        val channelUrlList: MutableList<Deferred<String>> = mutableListOf()
        coroutineScope {
            videos.map { video: YoutubeVideo ->
                val channelImgUrl = async {
                    try {
                        val channelResponse = apiService.fetchChannels(video.snippet.channelId).body()
                        channelResponse?.items?.first()?.snippet?.thumbnails?.medium?.url ?: ""
                    } catch (ex: NoSuchElementException) {
                        ex.printStackTrace()
                        DEFAULT_CHANNEL_IMG_LINK                    }
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

    private fun updateCurrentPageToken(nextPageToken: String) {
        sourceCurrentPageToken = nextPageToken
    }
}
