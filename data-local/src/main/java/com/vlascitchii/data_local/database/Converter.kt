package com.vlascitchii.data_local.database

import androidx.room.TypeConverter
import com.vlascitchii.data_local.enetity.PageEntity
import com.vlascitchii.data_local.enetity.video_list.ThumbnailAttributesEntity
import com.vlascitchii.data_local.enetity.video_list.ThumbnailsEntity
import com.vlascitchii.data_local.enetity.video_list.videos.ContentDetailsEntity
import com.vlascitchii.data_local.enetity.video_list.videos.VideoSnippetEntity
import com.vlascitchii.data_local.enetity.video_list.videos.VideoStatisticsEntity
import com.vlascitchii.data_local.enetity.video_list.videos.YoutubeVideoEntity
import com.vlascitchii.data_local.enetity.video_list.videos.YoutubeVideoResponseEntity
import com.vlascitchii.domain.enetity.video_list.ThumbnailAttributes
import com.vlascitchii.domain.enetity.video_list.Thumbnails
import com.vlascitchii.domain.enetity.video_list.videos.ContentDetails
import com.vlascitchii.domain.enetity.video_list.videos.VideoSnippet
import com.vlascitchii.domain.enetity.video_list.videos.VideoStatistics
import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideo
import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideoResponse
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

class Converter {

    private val offsetFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

    @TypeConverter
    fun fromOffsetDateTime(date: OffsetDateTime): String = date.format(offsetFormatter)

    @TypeConverter
    fun toOffsetDateTime(stringDate: String): OffsetDateTime = with(stringDate) {
        offsetFormatter.parse(this, OffsetDateTime::from)
    }
}

internal fun YoutubeVideoResponseEntity.convertToDomainYoutubeVideoResponse(): YoutubeVideoResponse {
    return YoutubeVideoResponse(
        nextPageToken = this.pageEntity.nextPageToken,
        currentPageToken = this.pageEntity.currentPageToken,
        prevPageToken = this.pageEntity.prevPageToken,
        items = this.items.convertToDomainYoutubeVideo()
    )
}

internal fun List<YoutubeVideoEntity>.convertToDomainYoutubeVideo(): List<YoutubeVideo> {
    return this.map { videoEntity: YoutubeVideoEntity ->
        YoutubeVideo(
            id = videoEntity.id,
            pageToken = videoEntity.pageToken,
            snippet = videoEntity.snippet.convertToDomainVideoSnippet(),
            statistics = videoEntity.statistics.convertToDomainVideoStatistics(),
            contentDetails = videoEntity.contentDetailsEntity.convertToDomainContentDetails()
        )
    }
}

internal fun VideoSnippetEntity.convertToDomainVideoSnippet(): VideoSnippet {
    return VideoSnippet(
        title = this.title,
        description = this.description,
        publishedAt = this.publishedAt,
        channelTitle = this.channelTitle,
        channelImgUrl = this.channelImgUrl,
        channelId = this.channelId,
        thumbnails = this.thumbnailsEntity.convertToDomainThumbnails()
    )
}

internal fun ThumbnailsEntity.convertToDomainThumbnails(): Thumbnails {
    return Thumbnails(
        medium = this.medium.convertToDomainThumbnailAttributes()

    )
}

internal fun ThumbnailAttributesEntity.convertToDomainThumbnailAttributes(): ThumbnailAttributes {
    return ThumbnailAttributes(
        url = this.url,
        height = this.height,
        width = this.width
    )
}

internal fun VideoStatisticsEntity.convertToDomainVideoStatistics(): VideoStatistics {
    return VideoStatistics(
        viewCount = this.viewCount,
        likeCount = this.likeCount
    )
}

internal fun ContentDetailsEntity.convertToDomainContentDetails(): ContentDetails {
    return ContentDetails(duration = this.duration)
}




internal fun YoutubeVideoResponse.convertToLocalYoutubeVideoResponseEntity(): YoutubeVideoResponseEntity {
    return YoutubeVideoResponseEntity(
        pageEntity = PageEntity(
            nextPageToken = this.nextPageToken,
            currentPageToken = this.currentPageToken,
            prevPageToken = this.prevPageToken

        ),
        items = this.items.convertToLocalYoutubeVideoEntity()
    )
}

internal fun List<YoutubeVideo>.convertToLocalYoutubeVideoEntity(): List<YoutubeVideoEntity> {
    return this.map { youTubeVide: YoutubeVideo ->
        YoutubeVideoEntity(
            id = youTubeVide.id,
            pageToken = youTubeVide.pageToken,
            snippet = youTubeVide.snippet.convertToLocalVideoSnippetEntity(),
            statistics = youTubeVide.statistics.convertToLocalVideoStatisticsEntity(),
            contentDetailsEntity = youTubeVide.contentDetails.convertToLocalContentDetailsEntity()
        )
    }
}

internal fun VideoSnippet.convertToLocalVideoSnippetEntity(): VideoSnippetEntity {
    return VideoSnippetEntity(
        title = this.title,
        description = this.description,
        publishedAt = this.publishedAt,
        channelTitle = this.channelTitle,
        channelImgUrl = this.channelImgUrl,
        channelId = this.channelId,
        thumbnailsEntity = this.thumbnails.convertToLocalVEntity()
    )
}

internal fun Thumbnails.convertToLocalVEntity(): ThumbnailsEntity {
    return ThumbnailsEntity(
        medium = this.medium.convertToLocalThumbnailAttributesEntity()
    )
}

internal fun ThumbnailAttributes.convertToLocalThumbnailAttributesEntity(): ThumbnailAttributesEntity {
    return ThumbnailAttributesEntity(
        url = this.url,
        height = this.height,
        width = this.width
    )
}

internal fun VideoStatistics.convertToLocalVideoStatisticsEntity(): VideoStatisticsEntity {
    return VideoStatisticsEntity(
        viewCount = this.viewCount,
        likeCount = this.likeCount
    )
}

internal fun ContentDetails.convertToLocalContentDetailsEntity(): ContentDetailsEntity {
    return ContentDetailsEntity(
        duration = this.duration
    )
}