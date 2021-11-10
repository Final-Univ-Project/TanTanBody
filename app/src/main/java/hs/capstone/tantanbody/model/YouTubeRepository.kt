package hs.capstone.tantanbody.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import hs.capstone.tantanbody.model.data.UserDto
import hs.capstone.tantanbody.model.data.YouTubeVideo
import hs.capstone.tantanbody.model.network.RetrofitClient
import retrofit2.Call
import retrofit2.Response
import java.util.*
import javax.security.auth.callback.Callback

class YouTubeRepository(val userEmail: String) {
    val TAG = "YouTubeRepository"
    var favYoutubeVideos: MutableLiveData<MutableList<YouTubeVideo>> =
        loadFavYouTubeVideo() as MutableLiveData<MutableList<YouTubeVideo>>
    var exerciseYouTubeVideos: MutableLiveData<MutableList<YouTubeVideo>> =
        loadExercisedVideos() as MutableLiveData<MutableList<YouTubeVideo>>

    // 즐겨찾기한 영상 가져오기
    fun loadFavYouTubeVideo() {
        // TODO. MutableLiveData<MutableList<YouTubeVideo>> 로 반환
        //       or List<YouTubeVideo>로 반환
        val call = RetrofitClient.myTestClientService
        lateinit var result: List<YouTubeVideo>
        call.getFavExercise(
            userEmail = userEmail
        ).enqueue(object : retrofit2.Callback<List<YouTubeVideo>> {
            override fun onResponse(
                call: Call<List<YouTubeVideo>>,
                response: Response<List<YouTubeVideo>>
            ) {
                Log.d(TAG, "isSuccessful: ${response.isSuccessful}")
                Log.d(TAG, "response: ${response.body()}")

                if (response.isSuccessful) {
                    result = response.body()!!
                }
            }
            override fun onFailure(call: Call<List<YouTubeVideo>>, t: Throwable) {
                Log.e(TAG, "error: $t")
            }
        })
    }
    // 즐겨찾기 영상 추가
    fun insertFavYoutubeVideo(video: YouTubeVideo) {
        Log.d(TAG, "insert videoId: ${video.videoId}")
        favYoutubeVideos.value?.add(video)
    }
    // 즐겨찾기 영상 삭제
    fun removeFavYouTubeVideo(video: YouTubeVideo) {
        // 나중에 서버 연결되면, 그냥 서버에 삭제명령 전달하는 식으로
        Log.d(TAG, "remove videoId: ${video.videoId}")
        favYoutubeVideos.value?.remove(video)
    }

    // 운동한 영상 키워드 가져오기
    fun loadExercisedVideos() = liveData<MutableList<YouTubeVideo>>{
        emit(mutableListOf())
    }
    // 클릭한 운동영상 추가
    fun insertClickedYouTube(now: Date, video: YouTubeVideo) {
        Log.d(TAG, "now: ${now} videoId: ${video.videoId}")
    }
    // 운동을 마친 운동영상 추가
    fun insertDoneYouTube(now: Date, video: YouTubeVideo) {
        Log.d(TAG, "now: ${now} videoId: ${video.videoId}")
    }
}