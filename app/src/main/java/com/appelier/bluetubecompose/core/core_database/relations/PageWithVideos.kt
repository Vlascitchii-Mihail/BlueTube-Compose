package com.appelier.bluetubecompose.core.core_database.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.appelier.bluetubecompose.screen_video_list.model.Page
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideo

data class PageWithVideos(
    @Embedded val page: Page,
    @Relation(
        parentColumn = "currentPageToken",
        entityColumn = "pageToken"
    )
    val videos: List<YoutubeVideo>
)
