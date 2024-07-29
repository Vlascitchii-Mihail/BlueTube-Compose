package com.appelier.bluetubecompose.screen_video_list.model.videos

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.appelier.bluetubecompose.core.core_database.OffsetDateTimeSerializer
import com.appelier.bluetubecompose.screen_video_list.model.Page
import com.appelier.bluetubecompose.screen_video_list.model.ThumbnailAttributes
import com.appelier.bluetubecompose.screen_video_list.model.Thumbnails
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.OffsetDateTime

@Entity(
    tableName = "youtube_video",
    foreignKeys = [
        ForeignKey(
            entity = Page::class,
            parentColumns = ["currentPageToken"],
            childColumns = ["pageToken"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
@JsonClass(generateAdapter = true)
@Serializable
@Parcelize
data class YoutubeVideo(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    @ColumnInfo(index = true)
    var pageToken: String = "",
    @Ignore
    var snippet: VideoSnippet = VideoSnippet(),
    @Ignore
    var statistics: VideoStatistics = VideoStatistics(),
    @Ignore
    @Contextual
    var contentDetails: ContentDetails = ContentDetails(),
    @field:Json(ignore = true)
    @Serializable(with = OffsetDateTimeSerializer::class)
    var loadedDate: OffsetDateTime? = null
)
    : Parcelable
{
    //Need a secondary constructor to be able to use @Ignore parameters in your primary constructor.
    // This is so Room still has a constructor that it can use when instantiating your object.
    constructor(id: String, pageToken: String)
    :this(id, pageToken, VideoSnippet(), VideoStatistics(), ContentDetails())

    companion object {
        private val thumbnailAttributes = ThumbnailAttributes(
            "https://i.ytimg.com/vi/m-4ZM3jxhdE/mqdefault.jpg",
            120,
            90
        )

        private val thumbnails = Thumbnails(
            thumbnailAttributes
        )

        private val snippet = VideoSnippet(
            title = "State of Play | May 30, 2024",
            description = "",
            publishedAt = "2024-05-30T22:00:12Z",
            channelTitle = "PlayStation",
            channelImgUrl = "https://yt3.ggpht.com/vIq4vVe_C7zq66KKHAtx89KagpDR1CuKkvgi96KtOaVgSTh67G3yJbTUkZ_o_ivUoG4Jxy9QkA=s240-c-k-c0x00ffffff-no-rj",
            channelId = "UC-2Y8dQb0S6DtpxNgAKoJKA",
            thumbnails = thumbnails
        )

        private val statistics = VideoStatistics(
            1372560,
            48971
        )

        private val contentDetails = ContentDetails(
            "PT35M32S"
        )

        val DEFAULT_VIDEO = YoutubeVideo(
            id = "m-4ZM3jxhdE",
            snippet = snippet,
            statistics = statistics,
            contentDetails = contentDetails
        )

        val DEFAULT_VIDEO_LIST = listOf(DEFAULT_VIDEO, DEFAULT_VIDEO, DEFAULT_VIDEO, DEFAULT_VIDEO)
    }
}
