package hs.capstone.tantanbody.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import hs.capstone.tantanbody.model.data.YouTubeVideo
import java.util.*

class YouTubeRepository {
    val TAG = "YouTubeRepository"
    var favYoutubeVideos: MutableLiveData<MutableList<YouTubeVideo>> =
        loadFavYouTubeVideo() as MutableLiveData<MutableList<YouTubeVideo>>
    var exerciseYouTubeVideos: MutableLiveData<MutableList<YouTubeVideo>> =
        loadExercisedVideos() as MutableLiveData<MutableList<YouTubeVideo>>

    fun loadFavYouTubeVideo() = liveData<MutableList<YouTubeVideo>> {
        emit(mutableListOf())
    }
    fun insertFavYoutubeVideo(video: YouTubeVideo) {
        Log.d(TAG, "insert videoId: ${video.videoId}")
        favYoutubeVideos.value?.add(video)
    }
    fun removeFavYouTubeVideo(video: YouTubeVideo) {
        // 나중에 서버 연결되면, 그냥 서버에 삭제명령 전달하는 식으로
        Log.d(TAG, "remove videoId: ${video.videoId}")
        favYoutubeVideos.value?.remove(video)
    }

    fun loadExercisedVideos() = liveData<MutableList<YouTubeVideo>>{
        emit(mutableListOf())
    }
    fun insertClickedYouTube(now: Date, video: YouTubeVideo) {
        Log.d(TAG, "now: ${now} videoId: ${video.videoId}")
    }
    fun insertDoneYouTube(now: Date, video: YouTubeVideo) {
        Log.d(TAG, "now: ${now} videoId: ${video.videoId}")
    }
}