package com.vlascitchii.data_local.enetity.video_list.videos

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.vlascitchii.data_local.enetity.INITIAL_PAGE_TOKEN
import com.vlascitchii.data_local.enetity.PageEntity
import java.time.OffsetDateTime

@Entity(
    tableName = "youtube_video",
    foreignKeys = [
        ForeignKey(
            entity = PageEntity::class,
            parentColumns = ["currentPageToken"],
            childColumns = ["pageToken"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class YoutubeVideoEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    @ColumnInfo(index = true)
    var pageToken: String = INITIAL_PAGE_TOKEN,
    @Ignore
    var snippet: VideoSnippetEntity = VideoSnippetEntity(),
    @Ignore
    var statistics: VideoStatisticsEntity = VideoStatisticsEntity(),
    @Ignore
    var contentDetailsEntity: ContentDetailsEntity = ContentDetailsEntity(),
    var loadedDate: OffsetDateTime? = null
)
{
    //Need a secondary constructor to be able to use @Ignore parameters in your primary constructor.
    // This is so Room still has a constructor that it can use when instantiating your object.
    constructor(id: String, pageToken: String)
    :this(id, pageToken, VideoSnippetEntity(), VideoStatisticsEntity(), ContentDetailsEntity())
}
