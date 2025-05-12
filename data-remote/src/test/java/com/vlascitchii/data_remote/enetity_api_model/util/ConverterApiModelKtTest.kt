package com.vlascitchii.data_remote.enetity_api_model.util

import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideoResponse.Companion.RESPONSE_VIDEO_LIST_NO_CHANNEL_IMG_URL
import junit.framework.TestCase.assertEquals
import org.junit.Test

class ConverterApiModelKtTest {
    private val expectedYoutubeVideoResponse = RESPONSE_VIDEO_LIST_NO_CHANNEL_IMG_URL
    private val expectedYoutubeVideoList = RESPONSE_VIDEO_LIST_NO_CHANNEL_IMG_URL.items

    private val expectedYoutubeVideoResponseApiModel = RESPONSE_VIDEO_LIST_NO_CHANNEL_IMG_URL.convertToYouTubeVideoResponseApiModel()
    private val expectedYoutubeVideoListApiModel = RESPONSE_VIDEO_LIST_NO_CHANNEL_IMG_URL.items.convertToYoutubeVideoApiModelList()


    @Test
    fun `convertToYouTubeVideoResponseApiModel converts YoutubeVideoResponse to YoutubeVideoResponseApiModel`() {
        val actualYoutubeVideoResponseApiModel = expectedYoutubeVideoResponse.convertToYouTubeVideoResponseApiModel()

        assertEquals(expectedYoutubeVideoResponseApiModel, actualYoutubeVideoResponseApiModel)
    }

    @Test
    fun `convertToYoutubeVideoApiModelList() converts YoutubeVideo List to YoutubeVideoApiModel List`() {
        val actualYoutubeVideoListApiModel = expectedYoutubeVideoList.convertToYoutubeVideoApiModelList()

        assertEquals(expectedYoutubeVideoListApiModel, actualYoutubeVideoListApiModel)
    }

    @Test
    fun `convertToYouTubeVideoResponse() converts YoutubeVideoResponseApiModel to YoutubeVideoResponse`() {
        val actualYoutubeVideoResponse = expectedYoutubeVideoResponseApiModel.convertToYouTubeVideoResponse()

        assertEquals(expectedYoutubeVideoResponse, actualYoutubeVideoResponse)
    }

    @Test
    fun `convertToYoutubeVideoList() converts YoutubeVideoApiModel list to YoutubeVideo List`() {
        val actualYoutubeVideoList = expectedYoutubeVideoListApiModel.convertToYoutubeVideoList()

        assertEquals(expectedYoutubeVideoList, actualYoutubeVideoList)

    }
}