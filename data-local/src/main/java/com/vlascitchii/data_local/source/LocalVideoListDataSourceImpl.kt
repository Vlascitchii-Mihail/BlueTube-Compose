package com.vlascitchii.data_local.source

import com.vlascitchii.data_local.database.convertToDomainYoutubeVideoResponse
import com.vlascitchii.data_local.database.convertToLocalYoutubeVideoResponseEntity
import com.vlascitchii.data_local.enetity.video_list.videos.YoutubeVideoResponseEntity
import com.vlascitchii.data_local.source.utils.DatabaseContentManager
import com.vlascitchii.data_repository.data_source.local.LocalVideoListDataSource
import com.vlascitchii.domain.custom_scope.CustomCoroutineScope
import com.vlascitchii.domain.model.videos.YoutubeVideoResponseDomain
import com.vlascitchii.domain.util.UseCaseException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.time.OffsetDateTime
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class LocalVideoListDataSourceImpl @Inject constructor(
    private val databaseContentManager: DatabaseContentManager,
    private val customCoroutineScope: CustomCoroutineScope
) : LocalVideoListDataSource {

    override suspend fun insertVideosToDatabaseWithTimeStamp(
        youTubeVideoResponse: YoutubeVideoResponseDomain,
        loadDate: OffsetDateTime,
    ) {
        try {
            with(databaseContentManager) {
                val videoResponseEntity =
                    youTubeVideoResponse.convertToLocalYoutubeVideoResponseEntity()
                        .setCurrentPageTokenToVideos(sourceCurrentPageToken)

                customCoroutineScope.linkWithParentContextAndGetContext(coroutineContext)
                customCoroutineScope.launch {
                    delay(10000L)

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

    override fun getVideosFromDatabase(pageToken: String): Flow<YoutubeVideoResponseDomain> = flow {

        with(databaseContentManager) {
            val videoResponse: Flow<YoutubeVideoResponseEntity> =
                if (pageToken == "") getFirstPageFromDatabase()
                else getParticularPageFromDatabase(pageToken)

            updateCurrentPageToken(videoResponse.first())
            emit(videoResponse.first().convertToDomainYoutubeVideoResponse())
        }
    }
        .flowOn(customCoroutineScope.coroutineContext)
        .catch { throw UseCaseException.LocalStorageException(it) }
}
