package com.appelier.bluetubecompose.screen_shorts.screen

import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.lifecycle.Lifecycle
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.coroutines.flow.MutableSharedFlow

private const val PLAYBACK_START_TIME = 0F

class ShortsPlayerHandler(
    private val currentComposeLifecycle: Lifecycle,
    private val videoId: String,
    private val videoQueue: MutableSharedFlow<YouTubePlayer?>
) {

    private var currentVideoPlayer: YouTubePlayer? = null


    fun setupPlayer(youTubePlayerView: YouTubePlayerView) {
        youTubePlayerView.apply {
            layoutParams = ViewGroup.LayoutParams(
                MATCH_PARENT,
                MATCH_PARENT
            )
            currentComposeLifecycle.addObserver(this)

            val youTubePlayerListener = getYouTubePlayerListener()
            addYouTubePlayerListener(youTubePlayerListener)
        }
    }

    private fun getYouTubePlayerListener(): AbstractYouTubePlayerListener {
        return object: AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                currentVideoPlayer = youTubePlayer
                videoQueue.tryEmit(youTubePlayer)
                youTubePlayer.cueVideo(videoId, PLAYBACK_START_TIME)
            }
        }
    }
}
