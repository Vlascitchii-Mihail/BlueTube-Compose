package com.appelier.bluetubecompose.screen_video_list.model.videos

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class ContentDetails(
    val duration: String = ""
): Parcelable