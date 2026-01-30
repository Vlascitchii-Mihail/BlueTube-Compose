package com.vlascitchii.data_remote.util

import com.vlascitchii.data_remote.model_api.API_ERROR_YOUTUBE_RESPONSE_INSTANCE
import com.vlascitchii.data_remote.source.util.MoshiParser
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class MoshiParserTest {

    private val moshiParser: MoshiParser = MockWebServerApiProvider.staticMoshiParser

    @Test
    fun `positive fun parseJsonIntoNewsApiErrorResponse() handles json response`() {
        val jsonAsString = MockWebServerApiProvider.staticJsonHandler.readJsonAsStringFromPath(ERROR_YOUTUBE_RESPONSE)

        val actualResult = moshiParser.parseJsonIntoNewsApiErrorResponse(jsonAsString)
        val expectedResult = API_ERROR_YOUTUBE_RESPONSE_INSTANCE

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `negative fun parseJsonIntoNewsApiErrorResponse() handles empty string`() {
        val emptyStringInput = ""

        val actualResult = moshiParser.parseJsonIntoNewsApiErrorResponse(emptyStringInput)

        assertNull(actualResult)
    }

    @Test
    fun `negative fun parseJsonIntoNewsApiErrorResponse() handles dummy string`() {
        val dummyStringInput = "Hello world!"

        val actualResult = moshiParser.parseJsonIntoNewsApiErrorResponse(dummyStringInput)

        assertNull(actualResult)
    }
}
