package com.appelier.bluetubecompose.search_video.model

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import com.appelier.bluetubecompose.screen_video_list.model.videos.VideoSnippet
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class SearchVideoItem(
    val id: SearchVideoItemId,
    val snippet: VideoSnippet
): Parcelable
