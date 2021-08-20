package hs.capstone.tantanbody.data

import android.content.Context
import android.util.Log
import com.google.api.client.http.HttpRequest
import com.google.api.client.http.HttpRequestInitializer
import com.google.api.client.http.HttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.model.SearchResult
import com.google.api.services.youtube.model.Thumbnail
import hs.capstone.tantanbody.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import java.io.IOException


class YouTubeSearchList(val context: Context) {
    val TAG = "YouTubeSearchList"

    // Q. values/string.xml에 있는 유튜브API키를 가져오기 위해 context를 매개변수도 두었는데요,
    // 그렇지 않고 context없이 다른 파일에서 값을 가져오는 방법이 있을까요?
    private val apiKey by lazy {  // YouTube API Key
        Log.d(TAG, "[YouTube API key]: ${context.getString(R.string.youtube_api_key)}")
        context.getString(R.string.youtube_api_key)
    }
    val scope = CoroutineScope(Dispatchers.Default)

    private val HTTP_TRANSPORT: HttpTransport = NetHttpTransport() // HTTP transport
    private val JSON_FACTORY: JsonFactory = JacksonFactory() // JSON factory
    private var NUMBER_OF_VIDEOS_RETURNED: Long = 5 // (페이지당 최대 50)
    private var youtube: YouTube? = null // API 요청할 Youtube object

    private var youTubeSearchList = mutableListOf<YouTubeVideo>()



    fun getMaxResults() : Long {
        return NUMBER_OF_VIDEOS_RETURNED
    }

    suspend fun loadYouTubeSearchItems(query: String = "운동") = scope.async {
        youtube = YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, HttpRequestInitializer {
            @Throws(IOException::class)
            fun initialize(it: HttpRequest) { }
        }).setApplicationName("TantanBody").build()

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


    fun printSearchResult(iteratorSearchResults: Iterator<SearchResult>, query: String) {
        println("\n=============================================================")
        println("   First $NUMBER_OF_VIDEOS_RETURNED videos for search on \"$query\".")
        println("=============================================================\n")

        if (!iteratorSearchResults.hasNext()) {
            println(" There aren't any results for your query.")
        }
        while (iteratorSearchResults.hasNext()) {
            val singleVideo = iteratorSearchResults.next()
            val rId = singleVideo.id
            val thumbnail = singleVideo.snippet.thumbnails["default"] as Thumbnail?

//            val youtubevideo = YouTubeVideo(
//                videoId = rId.videoId,
//                publishedAt = singleVideo.snippet.publishedAt,
//                channelId = singleVideo.snippet.channelId,
//                title = singleVideo.snippet.title,
//                description = singleVideo.snippet.description,
//                thumbnail = thumbnail!!.url,
//                channelTitle = singleVideo.snippet.channelTitle
//            )
//            youTubeSearchList.add(youtubevideo)
//            Log.d(TAG, "${youtubevideo}")

            // Double checks the kind is video.
            if (rId.kind == "youtube#video") {

                println(" Video Id" + rId.videoId)
                println(" Title: " + singleVideo.snippet.title)
                println(" Thumbnail: " + thumbnail!!.url)
                println("\n-------------------------------------------------------------\n")
            }
        }
    }

}

