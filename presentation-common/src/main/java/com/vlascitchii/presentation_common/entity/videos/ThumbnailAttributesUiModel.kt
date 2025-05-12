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
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Parcelize
@Serializable
data class ThumbnailAttributesUiModel(
    @Serializable(with = URLSanitizer::class)
    val url: String = "",
    val height: Int = 0,
    val width: Int = 0
)
    : Parcelable

object URLSanitizer: KSerializer<String> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("URLEncodeString", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: String) {
        val encodedLink = URLEncoder.encode(value, StandardCharsets.UTF_8.toString())
        encoder.encodeString(encodedLink)
    }

    override fun deserialize(decoder: Decoder): String {
        return URLDecoder.decode(decoder.decodeString(), StandardCharsets.UTF_8.toString())
    }
}