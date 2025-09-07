package com.vlascitchii.data_remote.model_api.util

import com.vlascitchii.data_remote.model_api.API_RESPONSE_VIDEO_WITH_CHANNEL_IMG
import com.vlascitchii.data_remote.model_api.DOMAIN_RESPONSE_VIDEO_WITH_CHANNEL_IMG
import com.vlascitchii.data_remote.model_api.video_api_model.convertToDomainYouTubeVideoResponse
import com.vlascitchii.domain.model.videos.YoutubeVideoResponseDomain
import junit.framework.TestCase.assertEquals
import org.junit.Test

class ConverterApiModelTest {

    @Test
    fun `convertToDomainYouTubeVideoResponse() converts YoutubeVideoResponseApiModel to YoutubeVideoResponseDomain`() {
        val actualYoutubeVideoResponse: YoutubeVideoResponseDomain =
            API_RESPONSE_VIDEO_WITH_CHANNEL_IMG.convertToDomainYouTubeVideoResponse()

        assertEquals(DOMAIN_RESPONSE_VIDEO_WITH_CHANNEL_IMG, actualYoutubeVideoResponse)
    }
}
