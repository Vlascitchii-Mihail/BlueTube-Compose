package com.vlascitchii.data_local.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.vlascitchii.data_local.enetity.video_list.videos.YoutubeVideoEntity
import com.vlascitchii.data_local.enetity.video_list.videos.YoutubeVideoResponseEntity
import com.vlascitchii.data_local.enetity.PageEntity

//data class PageWithVideos(
//    @Embedded val pageEntity: PageEntity,
//    @Relation(
//        parentColumn = "currentPageToken",
//        entityColumn = "pageToken"
//    )
//    val videos: List<YoutubeVideoEntity>
//)

//fun PageWithVideos.convertToYoutubeVideoResponseEntity(): YoutubeVideoResponseEntity {
//    val page = this.pageEntity
//    val videos = this.videos
//    return YoutubeVideoResponseEntity(
//        nextPageToken = page.nextPageToken,
//        currentPageToken = page.currentPageToken,
//        prevPageToken = page.prevPageToken,
//        videos
//    )
//}
