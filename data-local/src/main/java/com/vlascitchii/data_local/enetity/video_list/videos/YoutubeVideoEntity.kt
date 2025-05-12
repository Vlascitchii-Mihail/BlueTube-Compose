package com.vlascitchii.data_local.enetity.video_list.videos

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.vlascitchii.data_local.enetity.PageEntity
import com.vlascitchii.data_local.enetity.video_list.ThumbnailAttributesEntity
import com.vlascitchii.data_local.enetity.video_list.ThumbnailsEntity
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
//@Serializable
//@Parcelize
data class YoutubeVideoEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    @ColumnInfo(index = true)
    var pageToken: String = "",
    @Ignore
    var snippet: VideoSnippetEntity = VideoSnippetEntity(),
    @Ignore
    var statistics: VideoStatisticsEntity = VideoStatisticsEntity(),
    @Ignore
//    @Contextual
    var contentDetailsEntity: ContentDetailsEntity = ContentDetailsEntity(),
//    @field:Json(ignore = true)
//    @Serializable(with = com.vlascitchii.data_local.database.OffsetDateTimeSerializer::class)
    var loadedDate: OffsetDateTime? = null
)
//    : Parcelable
{
    //Need a secondary constructor to be able to use @Ignore parameters in your primary constructor.
    // This is so Room still has a constructor that it can use when instantiating your object.
    constructor(id: String, pageToken: String)
    :this(id, pageToken, VideoSnippetEntity(), VideoStatisticsEntity(), ContentDetailsEntity())

//    companion object {
//        private val thumbnailAttributesEntity = ThumbnailAttributesEntity(
//            "https://i.ytimg.com/vi/m-4ZM3jxhdE/mqdefault.jpg",
//            120,
//            90
//        )
//
//        private val thumbnailsEntity = ThumbnailsEntity(
//            thumbnailAttributesEntity
//        )
//
//        private val snippet = VideoSnippetEntity(
//            title = "State of Play | May 30, 2024",
//            description = "",
//            publishedAt = "2024-05-30T22:00:12Z",
//            channelTitle = "PlayStation",
//            channelImgUrl = "https://yt3.ggpht.com/vIq4vVe_C7zq66KKHAtx89KagpDR1CuKkvgi96KtOaVgSTh67G3yJbTUkZ_o_ivUoG4Jxy9QkA=s240-c-k-c0x00ffffff-no-rj",
//            channelId = "UC-2Y8dQb0S6DtpxNgAKoJKA",
//            thumbnailsEntity = thumbnailsEntity
//        )
//
//        private val statistics = VideoStatisticsEntity(
//            1372560,
//            48971
//        )
//
//        private val contentDetailsEntity = ContentDetailsEntity(
//            "PT35M32S"
//        )
//
//        val DEFAULT_VIDEO = YoutubeVideoEntity(
//            id = "m-4ZM3jxhdE",
//            snippet = snippet,
//            statistics = statistics,
//            contentDetailsEntity = contentDetailsEntity
//        )
//
//        val DEFAULT_VIDEO_LIST = listOf(DEFAULT_VIDEO, DEFAULT_VIDEO, DEFAULT_VIDEO, DEFAULT_VIDEO)
//    }
}
