package com.appelier.bluetubecompose.core.core_database

import androidx.room.TypeConverter
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
