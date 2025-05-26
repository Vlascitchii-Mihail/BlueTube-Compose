package com.vlascitchii.data_local.database

import com.vlascitchii.data_local.enetity.video_list.videos.YoutubeVideoResponseEntity.Companion.TEST_DATABASE_VIDEO_RESPONSE
import com.vlascitchii.data_local.source.utils.assertListEqualsTo
import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideoResponse.Companion.RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG
import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideoResponse.Companion.testDateTime
import org.junit.Assert.assertEquals
import org.junit.Test

class ConverterTest {

    private val dbConverter = Converter()
    private val dateInString = "2007-12-03T10:15:30+01:00"

    @Test
    fun `fromOffsetDateTime() converts OffsetDateTime to string`() {
        val result = dbConverter.fromOffsetDateTime(testDateTime)

        assertEquals(dateInString, result)

    }

    @Test
    fun `toOffsetDateTime() converts dateInString to OffsetDateTime`() {
        val result = dbConverter.toOffsetDateTime(dateInString)

        assertEquals(testDateTime, result)
    }

    @Test
    fun `convertToDomainYoutubeVideoResponse() converts YoutubeVideoResponseEntity to YoutubeVideoResponse`() {
        val result = TEST_DATABASE_VIDEO_RESPONSE.convertToDomainYoutubeVideoResponse()

        assertEquals(RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG.nextPageToken, result.nextPageToken)
        assertEquals(RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG.currentPageToken, result.currentPageToken)
        assertEquals(RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG.prevPageToken, result.prevPageToken)

        RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG.items.assertListEqualsTo(result.items)
    }

    @Test
    fun `convertToLocalYoutubeVideoResponseEntity() converts YoutubeVideoResponse to YoutubeVideoResponseEntity`() {
        val result = RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG.convertToLocalYoutubeVideoResponseEntity()

        assertEquals(TEST_DATABASE_VIDEO_RESPONSE.pageEntity, result.pageEntity)
    }
}