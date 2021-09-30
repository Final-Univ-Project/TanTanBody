package hs.capstone.tantanbody.ui

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import hs.capstone.tantanbody.R

class YoutubeVideoActivity : YouTubeBaseActivity() {
    lateinit var youtubeVideoView: YouTubePlayerView
    lateinit var youtubePlayerInit : YouTubePlayer.OnInitializedListener
    lateinit var youtubeVideoPlayBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube_video)
        youtubeVideoView = findViewById(R.id.youtubeVideoView)
        youtubeVideoPlayBtn = findViewById(R.id.youtubeVideoPlayBtn)

        youtubePlayerInit = object : YouTubePlayer.OnInitializedListener {
            override fun onInitializationSuccess(
                p0: YouTubePlayer.Provider?,
                p1: YouTubePlayer?,
                p2: Boolean
            ) {
                p1?.loadVideo(VIDEO_ID)
            }

            override fun onInitializationFailure(
                p0: YouTubePlayer.Provider?,
                p1: YouTubeInitializationResult?
            ) {
                Toast.makeText(applicationContext, getString(R.string.fail_loading_youtube), Toast.LENGTH_LONG)
            }
        }
        youtubeVideoView.initialize(getString(R.string.youtube_api_key), youtubePlayerInit)

        youtubeVideoPlayBtn.setOnClickListener {
        }
    }

    companion object {
        lateinit var VIDEO_ID: String
    }
}