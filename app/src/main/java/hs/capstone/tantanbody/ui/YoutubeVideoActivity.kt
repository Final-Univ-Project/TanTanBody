package hs.capstone.tantanbody.ui

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import hs.capstone.tantanbody.R
import hs.capstone.tantanbody.model.data.YouTubeVideo

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
                p1?.loadVideo(video.videoId)
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

    override fun onBackPressed() {
        super.onBackPressed()
        buildDoneDialog().show()
    }

    fun buildDoneDialog(): AlertDialog.Builder {
        val doneDlg: AlertDialog.Builder = AlertDialog.Builder(
            baseContext,
            android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth
        )
        doneDlg.setTitle("운동을 마치겠어요?")
        doneDlg.setNegativeButton("취소", null)
        doneDlg.setPositiveButton("확인",
            // 운동을 마치고, 영상목록 화면으로 돌아가면
            DialogInterface.OnClickListener { dialog, which ->
                setResult(Activity.RESULT_OK, Intent())
            }
        )
        return doneDlg
    }

    override fun onDestroy() {
        super.onDestroy()
        setResult(Activity.RESULT_OK, Intent())
    }

    companion object {
        lateinit var video: YouTubeVideo
        fun newInstance(video: YouTubeVideo): YouTubeBaseActivity {
            this.video = video
            return YoutubeVideoActivity()
        }
    }
}