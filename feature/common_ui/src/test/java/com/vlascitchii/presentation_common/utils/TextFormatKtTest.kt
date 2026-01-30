package com.vlascitchii.presentation_common.utils

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.time.LocalDateTime

@RunWith(MockitoJUnitRunner::class)
class TextFormatKtTest {
    private val minutesAgo = LocalDateTime.now().minusMinutes(3L).toString().substring(0, 19)+ "Z"
    private val hoursAgo = LocalDateTime.now().minusHours(3L).toString().substring(0, 19)+ "Z"
    private val daysAgo = LocalDateTime.now().minusDays(3L).toString().substring(0, 19)+ "Z"
    private val monthsAgo = LocalDateTime.now().minusMonths(4L).toString().substring(0, 19)+ "Z"
    private val yearsAgo = LocalDateTime.now().minusYears(4L).toString().substring(0, 19)+ "Z"

    @Test
    fun `fun formatViews() converts views count into thousand - K, millions - M`() {
        val testThousandViews = 2_000L
        val testMillionViews = 2_000_000L

        val actualThousandViews = formatViews(testThousandViews)
        val actualMillionViews = formatViews(testMillionViews)

        assertEquals("2K", actualThousandViews)
        assertEquals("2M", actualMillionViews)
    }

    @Test
    fun `fun testFormatDate measures time ago`() {
        assertEquals("3 minutes ago",
            formatDate(minutesAgo)
        )
        assertEquals("3 hours ago",
            formatDate(hoursAgo)
        )
        assertEquals("3 days ago",
            formatDate(daysAgo)
        )
        assertEquals("4 months ago",
            formatDate(monthsAgo)
        )
        assertEquals("4 years ago",
            formatDate(yearsAgo)
        )
    }

    @Test
    fun `fun formatVideoDuration() converts PT2M2S to ordinary time format`() {
        val testDuration = "PT2M2S"
        val expectedTestResult = "02:02"

        val actualDurationResult = formatVideoDuration(testDuration)

        assertEquals(expectedTestResult, actualDurationResult)
    }
}