package hs.capstone.tantanbody.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.google.api.client.http.HttpRequest
import com.google.api.client.http.HttpRequestInitializer
import com.google.api.client.http.HttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.model.SearchResult
import com.google.api.services.youtube.model.Thumbnail
import hs.capstone.tantanbody.model.YouTubeRepository
import hs.capstone.tantanbody.model.data.YouTubeVideo
import kotlinx.coroutines.*
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL
import kr.co.shineware.nlp.komoran.core.Komoran
import java.io.IOException
import java.lang.IllegalArgumentException
import java.util.*

class YouTubeViewModel(private val repo: YouTubeRepository) : ViewModel() {
    val TAG = "YouTubeViewModel"
    val scope = CoroutineScope(Job() + Dispatchers.Default)
    private val HTTP_TRANSPORT: HttpTransport = NetHttpTransport() // HTTP transport
    private val JSON_FACTORY: JsonFactory = JacksonFactory() // JSON factory
    private var NUMBER_OF_VIDEOS_RETURNED: Long = 5 // (페이지당 최대 50)
    private var youtube: YouTube? = null // API 요청할 Youtube object
    var youtubeVideos = MutableLiveData<MutableList<YouTubeVideo>>()

    var favYoutubeVideos: LiveData<MutableList<YouTubeVideo>> = repo.favYoutubeVideos
    var historyExer: MutableMap<String, Long> = mutableMapOf()

    // 즐겨찾기 운동영상 추가
    fun insertFavYouTubeVideo(video: YouTubeVideo) {
        repo.insertFavYoutubeVideo(video)
    }
    // 즐겨찾기 운동영상 삭제
    fun deleteFavYouTubeVideo(video: YouTubeVideo) {
        repo.removeFavYouTubeVideo(video)
    }

    // 클릭한 운동영상 추가
    fun insertClickedYouTube(video: YouTubeVideo) {
        repo.insertClickedYouTube(Date(), video)
        // 운동시작 시간 기록
        historyExer.put(video.videoId, Date().time)
    }
    // 운동을 마친 운동영상 추가
    fun insertDoneYouTube(video: YouTubeVideo) {
        repo.insertDoneYouTube(Date(), video)

        // 운동끝시간과 같이 운동시간 계산
        // TODO. (프론트) 예외 및 오류처리
        val interT = getInterTime(historyExer[video.videoId]!!, Date().time)
        historyExer.put(video.videoId, interT.toLong())
    }
    // 시작~끝 운동시간
    fun getInterTime(inDate: Long, outDate: Long): Int {
        val diffMillies = Math.abs(inDate - outDate)
//        val sec = diffMillies/1000
        return (diffMillies/60000).toInt() // min
    }

    // 운동영상 형태소분석으로 키워드 반환
    fun getYouTubeVideoKeywords(sentence: String): String {
        val komoraNouns = Komoran(DEFAULT_MODEL.LIGHT).analyze(sentence).nouns
        return komoraNouns.joinToString(" #", "#", "", 30, "...")
    }
    // SearchResult 를 YouTubeVideo 로 변환
    fun bind2YouTubeVideo(results: List<SearchResult>) {
        results.forEach {
            val keywords = getYouTubeVideoKeywords(it.snippet.title)
            val thumbnail = it.snippet.thumbnails["high"] as Thumbnail
            // YouTubeVideo() 바인딩
            youtubeVideos.value!!.add(YouTubeVideo(
                videoId = it.id.videoId,
                publishedAt = it.snippet.publishedAt,
                channelId = it.snippet.channelId,
                title = it.snippet.title,
                description = it.snippet.description,
                thumbnail = thumbnail.url,
                channelTitle = it.snippet.channelTitle,
                keywords = keywords
            ))
        }
    }
    // TODO. (프론트) 추가로 영상 가져오기

    // HTTP를 통해 유튜브 검색 목록 가져오는 메소드
    fun loadYouTubeSearchItems(apiKey: String): MutableLiveData<MutableList<YouTubeVideo>> {
        if (youtubeVideos.value != null) {
            return youtubeVideos
        }

        try {
            runBlocking {
                val resultResponse = buildYouTubeSearchItems(apiKey = apiKey).await()
                Log.d(TAG, "isEmpty: ${resultResponse?.isEmpty()}")
                Log.d(TAG, "items.size: ${resultResponse.items.size}")

                youtubeVideos.value = mutableListOf()
                bind2YouTubeVideo(resultResponse.items)
            }
        } catch (e: GoogleJsonResponseException) {
            Log.e(TAG, "[service error]: ${e.details.code} : ${e.details.message}")
        } catch (e: IOException) {
            Log.e(TAG, "[IO error]: ${e.cause} : ${e.message}")
        } catch (t: Throwable) {
            Log.e(TAG, "[Throwed error]: ${t.message}")
            t.printStackTrace()
        }
        return youtubeVideos
    }
    suspend fun buildYouTubeSearchItems(query: String = "운동", apiKey: String) = scope.async {
        youtube = YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, HttpRequestInitializer {
            @Throws(IOException::class)
            fun initialize(it: HttpRequest) { }
        }).setApplicationName("TanTanBody").build()
        val search = youtube!!.Search().list(mutableListOf("id", "snippet"))

        search.key = apiKey
        search.q = query
        search.type = listOf("video")
        search.fields = "items(id/kind,id/videoId,"+
                "snippet/publishedAt,snippet/channelId,"+
                "snippet/title,snippet/description,"+
                "snippet/thumbnails/high,snippet/channelTitle)"
        search.maxResults = NUMBER_OF_VIDEOS_RETURNED
        search.execute()
    }
}

class YouTubeViewModelFactory(private val repo: YouTubeRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(YouTubeViewModel::class.java)) {
            return YouTubeViewModel(repo) as T
        }
        throw IllegalArgumentException("알 수 없는 ViewModel 클래스")
    }
}