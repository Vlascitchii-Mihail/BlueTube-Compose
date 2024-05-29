package com.appelier.bluetubecompose.core.core_api

import com.appelier.bluetubecompose.BuildConfig

class Constants {

    companion object {

        const val API_KEY = BuildConfig.API_KEY

        const val BASE_URL = "https://www.googleapis.com/youtube/v3/"
        const val LIST_OF_VIDEOS = "videos"
        const val CHANNELS = "channels"
        const val SEARCH = "search"

        const val SNIPPET = "snippet"
        const val CONTENT_DETAILS = "contentDetails"
        const val STATISTICS = "statistics"
        const val MOST_POPULAR = "mostPopular"
        const val REGION_CODE = "US"
        const val SINGLE_CHANNEL = 1
        const val RELEVANCE = "relevance"

        const val CONTENT_VIDEO_TYPE = "video"
        const val SHORTS_VIDEO_DURATION = "short"

        const val INPUT_DELAY: Long = 1000

        const val FIRST_ELEMENT = 0
        const val PLAYER_LOAD_VIDEO_DELAY = 2000L
    }
}