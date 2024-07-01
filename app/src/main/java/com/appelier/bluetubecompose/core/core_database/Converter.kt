package com.appelier.bluetubecompose.core.core_database

import androidx.room.TypeConverter
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