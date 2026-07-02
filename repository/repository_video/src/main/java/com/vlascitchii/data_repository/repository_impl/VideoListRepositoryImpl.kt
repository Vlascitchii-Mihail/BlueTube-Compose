package com.vlascitchii.data_repository.repository_impl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.vlascitchii.data_repository.data_source.local.LocalVideoListDataSource
import com.vlascitchii.data_repository.data_source.remote.RemoteSearchDataSource
import com.vlascitchii.data_repository.data_source.remote.RemoteVideoListDataSource
import com.vlascitchii.data_repository.paging.VideoPagingSource
import com.vlascitchii.domain.di_common.DATABASE_SOURCE
import com.vlascitchii.domain.di_common.REMOTE_SEARCH_VIDEO_OR_SHORTS_LIST_SOURCE
import com.vlascitchii.domain.di_common.REMOTE_VIDEO_LIST_SOURCE
import com.vlascitchii.domain.model.videos.YoutubeVideoDomain
import com.vlascitchii.domain.model.videos.YoutubeVideoResponseDomain
import com.vlascitchii.domain.repository.VideoListRepository
import com.vlascitchii.domain.util.UseCaseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.time.OffsetDateTime
import javax.inject.Inject
import javax.inject.Named

internal const val PAGING_DEFAULT_PAGE_SIZE = 5
internal const val PREPARE_EXTRA_PAGES_MODIFIER = 3

class VideoListRepositoryImpl @Inject constructor(
    @param:Named(REMOTE_VIDEO_LIST_SOURCE)
    private val remoteVideoListDataSource: RemoteVideoListDataSource,
    @param:Named(REMOTE_SEARCH_VIDEO_OR_SHORTS_LIST_SOURCE)
    private val remoteSearchDataSource: RemoteSearchDataSource,
    @param:Named(DATABASE_SOURCE)
    private val databaseVideoSourceImpl: LocalVideoListDataSource,
) : VideoListRepository {

    private val pagerConfig = PagingConfig(
        pageSize = PAGING_DEFAULT_PAGE_SIZE,
        prefetchDistance = PAGING_DEFAULT_PAGE_SIZE * PREPARE_EXTRA_PAGES_MODIFIER
    )

    override fun getPopularVideos(): Flow<PagingData<YoutubeVideoDomain>> =
        Pager(
            config = pagerConfig,
            pagingSourceFactory = {
                VideoPagingSource(
                    { page: String ->
                        getVideoListAndCache(page)
                    }
                )
            }
        ).flow

    override fun getSearchVideos(query: String): Flow<PagingData<YoutubeVideoDomain>> =
        Pager(
            config = pagerConfig,
            pagingSourceFactory = {
                VideoPagingSource() { page: String ->
                    getVideoListAndCache(page, query)
                }
            }
        ).flow

    private suspend fun getVideoListAndCache(page: String, query: String = ""): YoutubeVideoResponseDomain {

        var youTubeVideoResponseDomain: YoutubeVideoResponseDomain = YoutubeVideoResponseDomain()

        try {
            youTubeVideoResponseDomain = if (query == "") {
                remoteVideoListDataSource.fetchVideos(page)
            } else {
                remoteSearchDataSource.searchVideos(query = query, nextPageToken = page)
            }

            databaseVideoSourceImpl.insertVideosWithTimeStamp(
                youTubeVideoResponseDomain,
                OffsetDateTime.now()
            )
        } catch(remoteEx: UseCaseException) {
            youTubeVideoResponseDomain = databaseVideoSourceImpl.getVideosFromStore(page).first()
        } catch (databaseEx: UseCaseException.LocalStorageException) {
            throw databaseEx
        } catch (ex: Throwable) {
            throw UseCaseException.UnknownException(ex)
        }

        return youTubeVideoResponseDomain
    }
}
