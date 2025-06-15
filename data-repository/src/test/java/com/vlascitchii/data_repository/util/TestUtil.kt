package com.vlascitchii.data_repository.util

import junit.framework.AssertionFailedError
import junit.framework.TestCase.assertEquals

fun <T> List<T>.assertListEqualsTo(actualList: List<T>?) {
    val expectedVideoList = this
    if (actualList.isNullOrEmpty()) throw AssertionFailedError()

    for (index in actualList.indices) {
        assertEquals(expectedVideoList[index], actualList[index])
    }
}