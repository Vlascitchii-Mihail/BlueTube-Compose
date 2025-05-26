package com.vlascitchii.data_local.source

import com.vlascitchii.data_local.database.convertToDomainYoutubeVideoResponse
import com.vlascitchii.data_local.database.convertToLocalYoutubeVideoResponseEntity
import com.vlascitchii.data_local.enetity.video_list.videos.YoutubeVideoResponseEntity
import com.vlascitchii.data_local.source.utils.DatabaseContentManager
import com.vlascitchii.data_repository.data_source.local.LocalVideoListDataSource
import com.vlascitchii.domain.custom_coroutine_scopes.AppCoroutineScope
import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideoResponse
import com.vlascitchii.domain.util.UseCaseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.time.OffsetDateTime
import javax.inject.Inject
import javax.inject.Named

class LocalVideoListDataSourceImpl @Inject constructor(
    @Named("video")
    private val videoCoroutineScope: AppCoroutineScope,
    private val databaseContentManager: DatabaseContentManager
) : LocalVideoListDataSource {

    override fun insertVideosToDatabaseWithTimeStamp(
        youTubeVideoResponse: YoutubeVideoResponse,
        loadDate: OffsetDateTime,
    ) {
        try {
            with(databaseContentManager) {
                val videoResponseEntity =
                    youTubeVideoResponse.convertToLocalYoutubeVideoResponseEntity()
                        .setCurrentPageTokenToVideos(sourceCurrentPageToken)

                println("videoResponseEntity = $videoResponseEntity")

                videoCoroutineScope.launch {
                    insertPageFrom(videoResponseEntity)
                    videoResponseEntity.bindAndInsertVideoWith(loadDate)
                    deleteExtraVideos()
                }

                updateCurrentPageToken(videoResponseEntity)
            }
        } catch (ex: Exception) {
            throw UseCaseException.LocalStorageException(ex)
        }
    }

    override fun getVideosFromDatabase(pageToken: String): Flow<YoutubeVideoResponse> = flow {

        with(databaseContentManager) {
            val videoResponse: Flow<YoutubeVideoResponseEntity> =
                if (pageToken == "") getFirstPageFromDatabase()
                else getParticularPageFromDatabase(pageToken)

            updateCurrentPageToken(videoResponse.first())
            emit(videoResponse.first().convertToDomainYoutubeVideoResponse())
        }
    }
        .flowOn(videoCoroutineScope.dispatcher)
        .catch { throw UseCaseException.LocalStorageException(it) }
}