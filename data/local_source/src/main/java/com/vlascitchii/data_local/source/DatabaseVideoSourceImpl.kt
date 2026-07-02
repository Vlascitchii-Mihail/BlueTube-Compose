package com.vlascitchii.data_local.source

import androidx.room.withTransaction
import com.vlascitchii.data_local.database.YouTubeDatabase
import com.vlascitchii.data_local.database.convertToDomainYoutubeVideoResponse
import com.vlascitchii.data_local.database.convertToLocalYoutubeVideoResponseEntity
import com.vlascitchii.data_local.entity.video_list.videos.YoutubeVideoResponseEntity
import com.vlascitchii.data_local.source.utils.DatabaseContentManager
import com.vlascitchii.data_repository.data_source.local.LocalVideoListDataSource
import com.vlascitchii.domain.model.videos.YoutubeVideoResponseDomain
import com.vlascitchii.domain.util.UseCaseException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.time.OffsetDateTime
import javax.inject.Inject

class DatabaseVideoSourceImpl @Inject constructor(
    private val youTubeDatabase: YouTubeDatabase,
    private val databaseContentManager: DatabaseContentManager,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : LocalVideoListDataSource {

    override suspend fun insertVideosWithTimeStamp(
        youTubeVideoResponse: YoutubeVideoResponseDomain,
        loadDate: OffsetDateTime
    ) {
        try {
            with(databaseContentManager) {

                val videoResponseEntity = youTubeVideoResponse
                        .convertToLocalYoutubeVideoResponseEntity()
                        .setCurrentPageTokenToVideos(sourceCurrentPageToken)

                withContext(dispatcher) {
                    youTubeDatabase.withTransaction {
                        insertPageFrom(videoResponseEntity)
                        videoResponseEntity.bindAndInsertVideoWith(loadDate)
                        deleteExtraVideos()
                    }

                    updateCurrentPageTokenForNextPage(videoResponseEntity)
                }
            }
        } catch (ex: Exception) {
            throw UseCaseException.LocalStorageException(ex)
        }
    }

    override fun getVideosFromStore(pageToken: String): Flow<YoutubeVideoResponseDomain> = flow {

        with(databaseContentManager) {
            val videoResponse: Flow<YoutubeVideoResponseEntity> =
                if (pageToken == "") getFirstPageFromDatabase()
                else getParticularPageFromDatabase(pageToken)

            videoResponse.first().let {
                updateCurrentPageTokenForNextPage(videoResponse.first())
                emit(it.convertToDomainYoutubeVideoResponse())
            }
        }
    }
        .flowOn(dispatcher)
        .catch { throw UseCaseException.LocalStorageException(it) }
}
