package com.appelier.bluetubecompose.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun ImageView.setImage(url: String, context: Context, viewModelScope: CoroutineScope) {
    viewModelScope.launch(Dispatchers.Main) {
        Glide.with(context).load(url).into(this@setImage)
    }
}