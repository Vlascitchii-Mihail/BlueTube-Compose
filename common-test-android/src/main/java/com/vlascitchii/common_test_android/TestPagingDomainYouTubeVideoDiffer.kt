package com.vlascitchii.common_test_android

import androidx.paging.AsyncPagingDataDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import com.vlascitchii.domain.enetity.video_list.videos.YoutubeVideo
import kotlinx.coroutines.CoroutineDispatcher

class TestPagingDomainYouTubeVideoDiffer(dispatcher: CoroutineDispatcher) {

    private object ListUpdateCallbackMock : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }

    private val diffCallback = object : DiffUtil.ItemCallback<YoutubeVideo>() {

        override fun areItemsTheSame(oldItem: YoutubeVideo, newItem: YoutubeVideo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: YoutubeVideo, newItem: YoutubeVideo): Boolean {
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