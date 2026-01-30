package com.vlascitchii.data_remote.util

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer

private const val SUCCESS_CODE = 200
internal const val ERROR_CODE = 400
private const val ROOT_PATH = "com/vlascitchii/data_remote/raw_response_json/"
const val VIDEO_LIST_RESPONSE_PATH = "${ROOT_PATH}video_list.json"
const val SEARCH_RESPONSE_PATH = "${ROOT_PATH}search_response.json"
const val CHANNEL_RESPONSE_PATH = "${ROOT_PATH}channel_response.json"
const val ERROR_YOUTUBE_RESPONSE = "${ROOT_PATH}error_youtube_response.json"
const val VIDEO_LIST_FROM_SEARCH_VIDEO_LIST_RESPONSE = "${ROOT_PATH}video_list_from_search_video_list_response.json"

class MockWebServerScheduler(
    private val mockWebServer: MockWebServer,
    private val staticJsonHandler: JsonHandler
) {

    fun shutDownMockWebServer() {
        mockWebServer.shutdown()
    }

    fun generateMockResponseFrom(jsonFilePath: String, code: Int = SUCCESS_CODE) {
        val videoResponse = MockResponse()
            .setBody(staticJsonHandler.readJsonAsStringFromPath(jsonFilePath))
            .setResponseCode(code)

        mockWebServer.enqueue(videoResponse)
    }
}
