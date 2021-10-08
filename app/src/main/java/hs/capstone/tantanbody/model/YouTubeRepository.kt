package hs.capstone.tantanbody.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.google.api.services.youtube.model.SearchResult
import com.google.api.services.youtube.model.Thumbnail
import hs.capstone.tantanbody.model.data.YouTubeVideo
import java.util.*

class YouTubeRepository {
    val TAG = "YouTubeRepository"
    var favYoutubeVideos: MutableLiveData<MutableMap<String, YouTubeVideo>> =
        loadFavYouTubeVideo() as MutableLiveData<MutableMap<String, YouTubeVideo>>
    var exerciseYouTubeVideos: MutableLiveData<MutableList<YouTubeVideo>> =
        loadExercisedVideos() as MutableLiveData<MutableList<YouTubeVideo>>

    fun loadFavYouTubeVideo() = liveData<MutableMap<String, YouTubeVideo>> {
        emit(mutableMapOf())
    }
    fun insertFavYoutubeVideo(video: YouTubeVideo) {
        Log.d(TAG, "insert videoId: ${video.videoId}")
        favYoutubeVideos.value?.put(video.videoId, video)
    }
    fun removeFavYouTubeVideo(videoId: String) {
        Log.d(TAG, "remove videoId: ${videoId}")
        favYoutubeVideos.value?.remove(videoId)
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