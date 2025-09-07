package com.vlascitchii.presentation_video_list.screen.state

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
sealed class VideoType : Parcelable {

    @Serializable
    @Parcelize
    object PopularVideo : VideoType(), Parcelable
    @Serializable
    @Parcelize
    data class SearchVideo(val query: String) : VideoType(), Parcelable
}