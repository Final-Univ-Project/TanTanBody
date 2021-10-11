package hs.capstone.tantanbody.ui

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import hs.capstone.tantanbody.R

class YoutubeVideoActivity : YouTubeBaseActivity() {
    val TAG = "YoutubeVideoActivity"
    lateinit var fullscreenVideo: YouTubePlayerView
    lateinit var apiKey: String
    lateinit var youtubePlayerInit: YouTubePlayer.OnInitializedListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube_video)
        fullscreenVideo = findViewById(R.id.fullscreenVideo)

        apiKey = getString(R.string.youtube_api_key)
        youtubePlayerInit = object : YouTubePlayer.OnInitializedListener {
            override fun onInitializationSuccess(
                p0: YouTubePlayer.Provider?,
                p1: YouTubePlayer?,
                p2: Boolean
            ) {
                p1?.loadVideo(videoId)
                Log.d(TAG, "isPlaying: ${p1?.isPlaying} p2: ${p2}")
            }

            override fun onInitializationFailure(
                p0: YouTubePlayer.Provider?,
                p1: YouTubeInitializationResult?
            ) {
                Log.e(TAG, "name: ${p1?.name} isUserRecoverableError: ${p1?.isUserRecoverableError}")
                Toast.makeText(baseContext, getString(R.string.fail_loading_youtube), Toast.LENGTH_SHORT).show()
            }
        }

        fullscreenVideo.initialize(apiKey, youtubePlayerInit)
    }

    companion object {
        lateinit var videoId: String
        fun newInstance(videoId: String): YouTubeBaseActivity {
            this.videoId = videoId
            return YoutubeVideoActivity()
        }
    }
}