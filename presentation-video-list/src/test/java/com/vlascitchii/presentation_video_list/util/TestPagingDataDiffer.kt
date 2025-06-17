package com.vlascitchii.presentation_video_list.util

import androidx.paging.AsyncPagingDataDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import com.vlascitchii.presentation_common.entity.videos.YoutubeVideoUiModel
import kotlinx.coroutines.CoroutineDispatcher

class TestPagingDataDiffer(dispatcher: CoroutineDispatcher) {

    private object ListUpdateCallbackMock : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }

    private val diffCallback = object : DiffUtil.ItemCallback<YoutubeVideoUiModel>() {

        override fun areItemsTheSame(oldItem: YoutubeVideoUiModel, newItem: YoutubeVideoUiModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: YoutubeVideoUiModel, newItem: YoutubeVideoUiModel): Boolean {
            return oldItem==newItem
        }
    }

    val pagingDiffer = AsyncPagingDataDiffer(
        diffCallback = diffCallback,
        updateCallback = ListUpdateCallbackMock,
        mainDispatcher = dispatcher,
        workerDispatcher = dispatcher
    )
}