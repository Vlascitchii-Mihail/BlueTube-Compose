package com.vlascitchii.data_local.source

import com.vlascitchii.data_local.database.YouTubeVideoDao
import com.vlascitchii.data_local.database.convertToDomainYoutubeVideoResponse
import com.vlascitchii.data_local.database.convertToLocalYoutubeVideoResponseEntity
import com.vlascitchii.data_local.enetity.video_list.videos.YoutubeVideoEntity
import com.vlascitchii.data_local.source.utils.DatabaseContentManager
import com.vlascitchii.data_local.enetity.video_list.videos.YoutubeVideoResponseEntity
import com.vlascitchii.data_repository.data_source.local.LocalVideoListDataSource
import com.vlascitchii.domain.custom_coroutine_scopes.AppCoroutineScope
import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideoResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.time.OffsetDateTime
import javax.inject.Inject
import javax.inject.Named


//TODO: try to call all the suspend functions in the same coroutine scope,
// so they run synchronously in background
class LocalVideoListDataSourceImpl @Inject constructor(
    private val youTubeVideoDao: YouTubeVideoDao,
    @Named("video")
    private val videoCoroutineScope: AppCoroutineScope
) : LocalVideoListDataSource {

    val databaseContentManager = DatabaseContentManager(youTubeVideoDao)

    override fun insertVideosToDatabaseWithTimeStamp(
        youTubeVideoResponse: YoutubeVideoResponse,
        loadDate: OffsetDateTime
    ) {
        with(databaseContentManager) {
            val videoResponseEntity = youTubeVideoResponse.convertToLocalYoutubeVideoResponseEntity()
                .setCurrentPageTokenToVideos(sourceCurrentPageToken)

            videoCoroutineScope.launch { insertPageFrom(videoResponseEntity)}

            videoResponseEntity.items.forEach { video: YoutubeVideoEntity ->
                video.bindVideosFromResponseWithData(loadDate)
                videoCoroutineScope.launch { youTubeVideoDao.insertVideo(video) }
            }

            videoCoroutineScope.launch { deleteExtraVideos() }
            updateCurrentPageToken(videoResponseEntity.pageEntity.nextPageToken)
        }
    }

    override fun getVideosFromDatabase(pageToken: String): Flow<YoutubeVideoResponse> = flow {

        with(databaseContentManager) {
            val videoResponse: Flow<YoutubeVideoResponseEntity> =
                if (pageToken == "") getFirstPageFromDatabase()
                else getParticularPageFromDatabase(pageToken)

            updateCurrentPageToken(videoResponse.first().pageEntity.nextPageToken)
            emit(videoResponse.first().convertToDomainYoutubeVideoResponse())
        }
    }.flowOn(videoCoroutineScope.dispatcher)
}