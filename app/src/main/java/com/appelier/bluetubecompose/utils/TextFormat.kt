package com.appelier.bluetubecompose.utils

//import androidx.appcompat.widget.SearchView
//import com.usm.bluetube.R
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
const val DAYS_PER_YEAR = 365
const val MONTHS_PER_YEAR = 12

const val VIDEO_LENGTH_SECONDS = 2

fun formatCount(views: Long): String {
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

fun formatVideoDuration(duration: String?): String {
    return duration?.replace("PT", "")?.replace("S", "")?.let { string ->
        if (string.length == VIDEO_LENGTH_SECONDS) "0:$string"
        else string
    }?.replace("[A-Z]".toRegex(), ":")
        ?: "null"
}

//fun SearchView.setupTextAppearance(context: Context, fontId: Int) {
//    val searchText = this.findViewById<View>(androidx.appcompat.R.id.search_src_text) as TextView
//    searchText.typeface = resources.getFont(fontId)
//    searchText.setTextColor(ContextCompat.getColor(context, R.color.black))
//}
//
//fun SearchView.setupBackground(context: Context, drawableId: Int) {
//    this.background = ContextCompat.getDrawable(context, drawableId)
//}