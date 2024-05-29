package com.appelier.bluetubecompose.screen_video_list.model

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class ThumbnailAttributes(
    val url: String = "",
    val height: Int = 0,
    val width: Int = 0
): Parcelable