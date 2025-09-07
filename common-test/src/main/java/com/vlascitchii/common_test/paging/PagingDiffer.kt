package com.vlascitchii.common_test.paging

import androidx.paging.AsyncPagingDataDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import kotlinx.coroutines.CoroutineDispatcher

class CommonTestPagingDiffer<T: Any>(
    dispatcher: CoroutineDispatcher,
    diffCallback: DiffUtil.ItemCallback<T>
) {

    private object ListUpdateCallbackMock : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }

    val pagingDiffer = AsyncPagingDataDiffer(
        diffCallback = diffCallback,
        updateCallback = ListUpdateCallbackMock,
        mainDispatcher = dispatcher,
        workerDispatcher = dispatcher
    )
}