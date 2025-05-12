package com.vlascitchii.presentation_common.utils

import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.abs

const val THOUSAND = 1_000
const val MILLION = 1_000_000
const val MILLIARD = 1_000_000_000
const val TRILLION = 1_000_000_000

const val SECONDS_PER_MINUTE = 60
const val SECONDS_PER_HOUR = 3600
const val HOURS_PER_DAY = 24
const val DAYS_PER_MONTH = 31
const val DAYS_PER_YEAR = 365

const val MINUTES_PER_HOUR = 60

fun formatViews(views: Long): String {
    return when(true) {
        (views < THOUSAND) -> views.toString()
        (views < MILLION) -> "${(views / THOUSAND).toInt()}K"
        (views < MILLIARD) -> "${(views / MILLION).toInt()}M"
        else -> "${(views / TRILLION).toInt()}MLR"
    }
}

//date = 2017-01-22T21:08:03Z
fun formatDate(date: String?): String {
    val postedDate: LocalDateTime = if (date != null) {
        LocalDateTime.parse(
            date.substring(0, 19),
            DateTimeFormatter.ISO_LOCAL_DATE_TIME
        )
    } else LocalDateTime.now()

    val currentDate = LocalDateTime.now()
    val postedAgo = Duration.between(currentDate, postedDate)
    val seconds = abs(postedAgo.seconds)
    val hours = abs(postedAgo.toHours())
    val days = abs(postedAgo.toDays())

    return when(true) {
        (seconds < SECONDS_PER_HOUR) -> "${seconds / SECONDS_PER_MINUTE} minutes ago"
        (hours < HOURS_PER_DAY) -> "$hours hours ago"
        (days < DAYS_PER_MONTH) -> "$days days ago"
        (days < DAYS_PER_YEAR) -> "${days / DAYS_PER_MONTH} months ago"
        (date == null) -> "null"
        else -> "${days / DAYS_PER_YEAR} years ago"
    }
}

//duration = PT2M2S
fun formatVideoDuration(duration: String): String {
    val videoDuration = Duration.parse(duration)

    val seconds = (videoDuration.seconds % SECONDS_PER_MINUTE).toString()
    val strSeconds = if (seconds.length == 1) "0$seconds" else seconds

    val minutes = (videoDuration.toMinutes() % MINUTES_PER_HOUR)
    val strMinutes = when(true) {
        (minutes in 1 .. 9) -> "0$minutes"
        (minutes > 9) -> minutes.toString()
        else -> "00"
    }

    val hours = videoDuration.toHours()
    val strHours = when(true) {
        (hours in 1..9) -> "0$hours"
        (hours > 9) -> hours.toString()
        else -> ""
    }

    return if (strHours == "") "$strMinutes:$strSeconds" else "$strHours:$strMinutes:$strSeconds"
}

//fun com.vlascitchii.data_local.database.relations.PageWithVideos.convertToYoutubeVideoResponse(): YoutubeVideoResponseApiModel {
//    val pageEntity = this.pageEntity
//    val videos = this.videos
//    return YoutubeVideoResponseApiModel(
//        nextPageToken = pageEntity.nextPageToken,
//        currentPageToken = pageEntity.currentPageToken,
//        prevPageToken = pageEntity.prevPageToken,
//        videos
//    )
//}