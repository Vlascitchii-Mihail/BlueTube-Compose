package com.appelier.bluetubecompose.utils

sealed class VideoType {

    data object Videos: VideoType()
    data object Shorts: VideoType()
    class SearchedVideo(var query: String): VideoType()
    class SearchedRelatedVideo(var query: String): VideoType()
}