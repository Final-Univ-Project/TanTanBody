package hs.capstone.tantanbody.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
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
import java.text.SimpleDateFormat
import java.util.*

class YouTubeViewModel(private val repo: YouTubeRepository) : ViewModel() {
    val TAG = "YouTubeViewModel"
    val scope = CoroutineScope(Job() + Dispatchers.Default)
    private val HTTP_TRANSPORT: HttpTransport = NetHttpTransport() // HTTP transport
    private val JSON_FACTORY: JsonFactory = JacksonFactory() // JSON factory
    private var NUMBER_OF_VIDEOS_RETURNED: Long = 5 // (페이지당 최대 50)
    private var youtube: YouTube? = null // API 요청할 Youtube object
    var youtubeVideoMap = mutableMapOf<String, YouTubeVideo>()

    var favYoutubeVideos: LiveData<MutableMap<String, YouTubeVideo>> = repo.favYoutubeVideos

    fun getInterTime(inDate: Long, outDate: Long): Float {
        val diffMillies = Math.abs(inDate - outDate)
        val sec = diffMillies/1000
        val min = sec/60
        return "${min}.${sec}".toFloat()
    }


    fun changeYoutubeVideoFav(video: YouTubeVideo, setFav: Boolean) {
        youtubeVideoMap[video.videoId]?.isFaverite = setFav
        Log.d(TAG, "videoId: ${video.videoId} isFaverite: ${video.isFaverite}")

        if (setFav) {
            insertFavYouTubeVideo(video)
        } else {
            deleteFavYouTubeVideo(video.videoId)
        }
    }
    fun insertFavYouTubeVideo(video: YouTubeVideo) {
        repo.insertFavYoutubeVideo(video)
    }
    fun deleteFavYouTubeVideo(videoId: String) {
        repo.removeFavYouTubeVideo(videoId)
    }

    fun insertClickedYouTube(video: YouTubeVideo) {
        repo.insertClickedYouTube(Date(), video)
    }
    fun insertDoneYouTube(video: YouTubeVideo) {
        repo.insertClickedYouTube(Date(), video)
    }

    fun getYouTubeVideoKeywords(sentence: String): List<String> {
        var komoran = Komoran(DEFAULT_MODEL.LIGHT).analyze(sentence)
        Log.d(TAG, "** sentence: ${sentence}")
        Log.d(TAG, "** nouns: ${komoran.nouns}")
        return komoran.nouns
    }
    fun convert2YouTubeVideo(results: List<SearchResult>): List<YouTubeVideo> {
        results.forEach {
            val keywords = getYouTubeVideoKeywords(it.snippet.title)
            val thumbnail = it.snippet.thumbnails["high"] as Thumbnail
            // YouTubeVideo() 바인딩
            youtubeVideoMap[it.id.videoId] = YouTubeVideo(
                videoId = it.id.videoId,
                publishedAt = it.snippet.publishedAt,
                channelId = it.snippet.channelId,
                title = it.snippet.title,
                description = it.snippet.description,
                thumbnail = thumbnail.url,
                channelTitle = it.snippet.channelTitle,
                keywords = keywords
            )
        }
        return youtubeVideoMap.values.toList()
    }

    // HTTP를 통해 유튜브 검색 목록 가져오는 메소드
    fun loadYouTubeSearchItems(apiKey: String): List<YouTubeVideo>? {
        try {
            val YoutubeVideos = runBlocking {
                val resultResponse = buildYouTubeSearchItems(apiKey = apiKey).await()
                Log.d(TAG, "isEmpty: ${resultResponse?.isEmpty()}")
                Log.d(TAG, "items.size: ${resultResponse.items.size}")

                convert2YouTubeVideo(resultResponse.items)
            }

            return YoutubeVideos

        } catch (e: GoogleJsonResponseException) {
            Log.e(TAG, "[service error]: ${e.details.code} : ${e.details.message}")
        } catch (e: IOException) {
            Log.e(TAG, "[IO error]: ${e.cause} : ${e.message}")
        } catch (t: Throwable) {
            Log.e(TAG, "[Throwed error]: ${t.message}")
            t.printStackTrace()
        }
        return null
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