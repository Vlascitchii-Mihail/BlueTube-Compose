package com.vlascitchii.data_local.entity.video_list.videos

import androidx.room.Embedded
import androidx.room.Relation
import com.vlascitchii.data_local.entity.INITIAL_PAGE_TOKEN
import com.vlascitchii.data_local.entity.PageEntity
import com.vlascitchii.data_local.entity.video_list.ThumbnailAttributesEntity
import com.vlascitchii.data_local.entity.video_list.ThumbnailsEntity
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

data class YoutubeVideoResponseEntity(
    @Embedded val pageEntity: PageEntity,
    @Relation(
        parentColumn = "currentPageToken",
        entityColumn = "pageToken"
    )
    val items: List<YoutubeVideoEntity>
)
