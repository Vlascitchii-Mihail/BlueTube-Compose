package com.vlascitchii.data_remote.util

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import java.io.File

private const val SUCCESS_CODE = 200
private const val ROOT_PATH =
    "D:\\MyPrograms\\Android\\BlueTubeCompose\\data-remote\\src\\test\\java\\com\\vlascitchii\\data_remote\\networking\\raw_response_json\\"
const val VIDEO_LIST_RESPONSE_PATH = "${ROOT_PATH}video_list.json"
const val PARTICULAR_VIDEO_RESPONSE_1_PATH = "${ROOT_PATH}particular_video_response_1.json"
const val PARTICULAR_VIDEO_RESPONSE_2_PATH = "${ROOT_PATH}particular_video_response_2.json"
const val PARTICULAR_VIDEO_RESPONSE_3_PATH = "${ROOT_PATH}particular_video_response_3.json"
const val PARTICULAR_VIDEO_RESPONSE_4_PATH = "${ROOT_PATH}particular_video_response_4.json"
const val PARTICULAR_VIDEO_RESPONSE_5_PATH = "${ROOT_PATH}particular_video_response_5.json"
const val SEARCH_RESPONSE_PATH = "${ROOT_PATH}search_response.json"
const val CHANNEL_RESPONSE_1_PATH = "${ROOT_PATH}channel_response_1.json"
const val CHANNEL_RESPONSE_2_PATH = "${ROOT_PATH}channel_response_2.json"
const val CHANNEL_RESPONSE_3_PATH = "${ROOT_PATH}channel_response_3.json"
const val CHANNEL_RESPONSE_4_PATH = "${ROOT_PATH}channel_response_4.json"
const val CHANNEL_RESPONSE_5_PATH = "${ROOT_PATH}channel_response_5.json"

class MockWebServerScheduler(private val mockWebServer: MockWebServer) {

    fun shutDownMockWebServer() {
        mockWebServer.shutdown()
    }

    fun generateMockResponseFrom(jsonFilePath: String) {
        val videoResponse = MockResponse().setBody(
            readJson(jsonFilePath)
        ).setResponseCode(SUCCESS_CODE)

        mockWebServer.enqueue(videoResponse)
    }

    fun getRequestCount() = mockWebServer.requestCount

    private fun readJson(jsonFilePath: String): String {
        return File(jsonFilePath).readText(Charsets.UTF_8)
    }
}