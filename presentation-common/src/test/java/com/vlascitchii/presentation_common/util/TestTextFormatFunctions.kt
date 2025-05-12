package com.vlascitchii.presentation_common.util

import com.vlascitchii.presentation_common.utils.formatDate
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.time.LocalDateTime

@RunWith(MockitoJUnitRunner::class)
class TestTextFormatFunctions {
    private val currentMinutes = LocalDateTime.now().plusMinutes(3L).toString().substring(0, 19)+ "Z"
    private val currentHours = LocalDateTime.now().plusHours(3L).toString().substring(0, 19)+ "Z"
    private val currentDays = LocalDateTime.now().plusDays(3L).toString().substring(0, 19)+ "Z"
    private val currentMonths = LocalDateTime.now().plusMonths(4L).toString().substring(0, 19)+ "Z"

    @Test
    fun testFormatDate() {
        assertEquals("2 minutes ago",
            com.vlascitchii.presentation_common.utils.formatDate(currentMinutes)
        )
        assertEquals("2 hours ago",
            com.vlascitchii.presentation_common.utils.formatDate(currentHours)
        )
        assertEquals("2 days ago",
            com.vlascitchii.presentation_common.utils.formatDate(currentDays)
        )
        assertEquals("3 months ago",
            com.vlascitchii.presentation_common.utils.formatDate(currentMonths)
        )
        assertEquals("7 years ago",
            com.vlascitchii.presentation_common.utils.formatDate("2017-01-22T21:08:03Z")
        )
    }

    @Test
    fun printDate() {
        println(currentMinutes)
    }
}