package hs.capstone.tantanbody.model

import android.util.Log
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
import hs.capstone.tantanbody.model.data.YouTubeVideo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.io.IOException

class YouTubeRepository {
    val TAG = "YouTubeRepository"
    val scope = CoroutineScope(Dispatchers.Default)
    private val HTTP_TRANSPORT: HttpTransport = NetHttpTransport() // HTTP transport
    private val JSON_FACTORY: JsonFactory = JacksonFactory() // JSON factory
    private var NUMBER_OF_VIDEOS_RETURNED: Long = 5 // (페이지당 최대 50)
    private var youtube: YouTube? = null // API 요청할 Youtube object
    var favoriteYoutubeList = mutableListOf<YouTubeVideo>()

    // List<SearchResult>를 List<YouTubeVideo>로 변환하는 메소드
    fun insertFavoriteYoutubeVideo(searchResult: SearchResult) {
//        while (searchResults.hasNext()) {
        val signleVideo = searchResult //.next()
        val thumbnail = signleVideo.snippet.thumbnails["default"] as Thumbnail?
        Log.d(TAG, "id.classInfo: ${signleVideo.id.classInfo}" +
                "id.channelId: ${signleVideo.id.channelId}" +
                "classInfo: ${signleVideo.classInfo}" +
                "height & width: ${thumbnail!!.height} ${thumbnail!!.width}" +
                "etag: ${signleVideo.etag}" +
                "kind: ${signleVideo.kind}" +
                "liveBroadcastContent: ${signleVideo.snippet.liveBroadcastContent}")

        val video = YouTubeVideo(
            videoId = signleVideo.id.videoId,
            publishedAt = signleVideo.snippet.publishedAt,
            channelId = signleVideo.snippet.channelId,
            title = signleVideo.snippet.title,
            description = signleVideo.snippet.description,
            thumbnail = thumbnail.url,
            channelTitle = signleVideo.snippet.channelTitle
        )
        favoriteYoutubeList.add(video)
    }

    // HTTP를 통해 유튜브 검색 목록 가져오는 메소드
    fun loadYouTubeSearchItems(apiKey: String): List<SearchResult>? {
        try {
            val searchResponse = runBlocking {
                buildYouTubeSearchItems(apiKey = apiKey).await()
            }
            Log.d(TAG, "isEmpty: ${searchResponse.isEmpty()}")

            Log.d(TAG, "searchResponse.items.size: ${searchResponse.items.size}")
            return searchResponse.items

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
                "snippet/thumbnails/default,snippet/channelTitle)"
        search.maxResults = NUMBER_OF_VIDEOS_RETURNED
        search.execute()
    }
}