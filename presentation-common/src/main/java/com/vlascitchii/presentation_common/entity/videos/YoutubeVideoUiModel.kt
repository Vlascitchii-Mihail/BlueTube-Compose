package com.vlascitchii.presentation_common.entity.videos

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@Serializable
@Parcelize
data class YoutubeVideoUiModel(
    val id: String,
    var pageToken: String = "",
    var snippet: VideoSnippetUiModel = VideoSnippetUiModel(),
    var statistics: VideoStatisticsUiModel = VideoStatisticsUiModel(),
//    @Contextual
    var contentDetails: ContentDetailsUiModel = ContentDetailsUiModel(),
//    @Serializable(with = OffsetDateTimeSerializer::class)
//    var loadedDate: OffsetDateTime? = null
)
    : Parcelable
{
    //Need a secondary constructor to be able to use @Ignore parameters in your primary constructor.
    // This is so Room still has a constructor that it can use when instantiating your object.
//    constructor(id: String, pageToken: String)
//    :this(id, pageToken, VideoSnippet(), VideoStatistics(), ContentDetails())

    companion object {
        private val thumbnailAttributes = ThumbnailAttributesUiModel(
            "https://i.ytimg.com/vi/m-4ZM3jxhdE/mqdefault.jpg",
            120,
            90
        )

        private val thumbnailsUiModel = ThumbnailsUiModel(
            thumbnailAttributes
        )

        private val snippet = VideoSnippetUiModel(
            title = "State of Play | May 30, 2024",
            description = "",
            publishedAt = "2024-05-30T22:00:12Z",
            channelTitle = "PlayStation",
            channelImgUrl = "https://yt3.ggpht.com/vIq4vVe_C7zq66KKHAtx89KagpDR1CuKkvgi96KtOaVgSTh67G3yJbTUkZ_o_ivUoG4Jxy9QkA=s240-c-k-c0x00ffffff-no-rj",
            channelId = "UC-2Y8dQb0S6DtpxNgAKoJKA",
            thumbnailsUiModel = thumbnailsUiModel
        )

        private val statistics = VideoStatisticsUiModel(
            1372560,
            48971
        )

        private val contentDetails = ContentDetailsUiModel(
            "PT35M32S"
        )

        val DEFAULT_VIDEO = YoutubeVideoUiModel(
            id = "m-4ZM3jxhdE",
            snippet = snippet,
            statistics = statistics,
            contentDetails = contentDetails
        )

        val DEFAULT_VIDEO_LIST = listOf(DEFAULT_VIDEO, DEFAULT_VIDEO, DEFAULT_VIDEO, DEFAULT_VIDEO)
    }
}

object OffsetDateTimeSerializer: KSerializer<OffsetDateTime> {
    private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("OffsetDateTime", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: OffsetDateTime) {
        encoder.encodeString(value.format(formatter))
    }

    override fun deserialize(decoder: Decoder): OffsetDateTime {
        return OffsetDateTime.parse(decoder.decodeString(), formatter)
    }
}
