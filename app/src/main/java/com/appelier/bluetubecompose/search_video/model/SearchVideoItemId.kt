package com.appelier.bluetubecompose.search_video.model

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class SearchVideoItemId(
    val videoId: String = ""
): Parcelable
