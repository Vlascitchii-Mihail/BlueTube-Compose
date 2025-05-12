package com.vlascitchii.data_remote.util

import com.vlascitchii.data_remote.enetity_api_model.video_list_api_model.videos_api_model.YoutubeVideoApiModel
import junit.framework.AssertionFailedError
import junit.framework.TestCase.assertEquals

fun <T> List<T>.assertListEqualsTo(actualList: List<T>?) {
    val expectedVideoList = this
    if (actualList.isNullOrEmpty()) throw AssertionFailedError()

    for (index in actualList.indices) {
        assertEquals(expectedVideoList[index], actualList[index])
    }
}

fun List<YoutubeVideoApiModel>?.sortLike(expectedList: List<YoutubeVideoApiModel>): MutableList<YoutubeVideoApiModel> {
    val actualList = this
    if (actualList.isNullOrEmpty() && actualList?.size != expectedList.size) throw AssertionFailedError()

    val newSortedList: MutableList<YoutubeVideoApiModel> = mutableListOf()

    expectedList.forEach { expectedElement ->
        newSortedList.add(actualList.first { actualElement -> actualElement.id == expectedElement.id})
    }
    return newSortedList
}
