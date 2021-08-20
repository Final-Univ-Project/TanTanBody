package hs.capstone.tantanbody

import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.google.api.services.youtube.model.SearchResult
import hs.capstone.tantanbody.data.YouTubeSearchList
import kotlinx.coroutines.*
import java.io.IOException


// Test용
class YouTubeSearchActivity: Activity() {
    val TAG = "YouTubeSearchActivity"
    val YTSearchList by lazy { YouTubeSearchList(this.baseContext) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get query from user
        var query = "운동"
        try {
            var searchResponse = runBlocking {
                YTSearchList.loadYouTubeSearchItems(query = query).await()
            }
            Log.d(TAG, "isEmpty: ${searchResponse.isEmpty()}")

            val searchResultList: List<SearchResult>? = searchResponse.items
            if (searchResultList != null) {
                YTSearchList.printSearchResult(searchResultList.iterator(), query)
            }
        } catch (e: GoogleJsonResponseException) {
            Log.e(TAG, "[service error]: ${e.details.code} : ${e.details.message}")
        } catch (e: IOException) {
            Log.e(TAG, "[IO error]: ${e.cause} : ${e.message}")
        } catch (t: Throwable) {
            Log.e(TAG, "[Throwed error]: ${t.message}")
            t.printStackTrace()
        }
    }

}