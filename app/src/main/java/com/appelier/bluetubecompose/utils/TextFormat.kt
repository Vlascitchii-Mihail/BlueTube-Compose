package com.appelier.bluetubecompose.utils

import com.appelier.bluetubecompose.core.core_database.relations.PageWithVideos
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideoResponse
import java.io.InputStreamReader
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

const val THOUSAND = 1_000
const val MILLION = 1_000_000
const val MILLIARD = 1_000_000_000
const val TRILLION = 1_000_000_000

const val SECONDS_PER_MINUTE = 60
const val SECONDS_PER_HOUR = 3600
const val HOURS_PER_DAY = 24
const val DAYS_PER_MONTH = 31
const val DAYS_PER_YEAR = 36

const val MINUTES_PER_HOUR = 60

fun formatViews(views: Long): String {
    return when(true) {
        (views < THOUSAND) -> views.toString()
        (views < MILLION) -> "${(views / THOUSAND).toInt()}K"
        (views < MILLIARD) -> "${(views / MILLION).toInt()}M"
        else -> "${(views / TRILLION).toInt()}MLR"
    }
}

fun formatDate(date: String?): String {
    val postedDate: LocalDateTime = if (date != null) {
        LocalDateTime.parse(
            date.substring(0, 19),
            DateTimeFormatter.ISO_LOCAL_DATE_TIME
        )
    } else LocalDateTime.now()

    val currentDate = LocalDateTime.now()
    val postedAgo = Duration.between(postedDate, currentDate)
    val seconds = postedAgo.seconds
    val hours = postedAgo.toHours()
    val days = postedAgo.toDays()

    return when(true) {
        (seconds < SECONDS_PER_HOUR) -> "${seconds / SECONDS_PER_MINUTE} minutes ago"
        (hours < HOURS_PER_DAY) -> "$hours hours ago"
        (days < DAYS_PER_MONTH) -> "$days days ago"
        (days < DAYS_PER_YEAR) -> "${days / DAYS_PER_MONTH} months ago"
        (date == null) -> "null"
        else -> "${days / DAYS_PER_YEAR} years ago"
    }
}

//PT2M2S
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

fun readJsonFileAsString(classLoader: ClassLoader?, path: String): String {
    return InputStreamReader(classLoader?.getResourceAsStream(path)).use { it.readText() }
}

fun  PageWithVideos.convertToYoutubeVideoResponse(): YoutubeVideoResponse {
    val page = this.page
    val videos = this.videos
    return YoutubeVideoResponse(
        nextPageToken = page.nextPageToken,
        currentPageToken = page.currentPageToken,
        prevPageToken = page.prevPageToken,
        videos
    )
}