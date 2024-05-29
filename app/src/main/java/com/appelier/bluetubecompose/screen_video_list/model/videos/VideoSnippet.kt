package com.appelier.bluetubecompose.screen_video_list.model.videos

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import com.appelier.bluetubecompose.screen_video_list.model.Thumbnails
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class VideoSnippet(
    val title: String,
    val description: String,
    val publishedAt: String,
    val channelTitle: String,
    var channelImgUrl: String = "",
    val channelId: String,
    val thumbnails: Thumbnails
): Parcelable
