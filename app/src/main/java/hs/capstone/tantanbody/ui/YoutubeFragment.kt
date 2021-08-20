package hs.capstone.tantanbody.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.google.api.services.youtube.model.SearchResult
import hs.capstone.tantanbody.R
import hs.capstone.tantanbody.data.YouTubeRecyclerAdapter
import hs.capstone.tantanbody.data.YouTubeSearchList
import kotlinx.coroutines.runBlocking
import java.io.IOException

class YoutubeFragment : Fragment() {
    val TAG = "YoutubeFragment"
    lateinit var YTListRecyclerView: RecyclerView

    lateinit var YTSearchList : YouTubeSearchList
    var searchResultList: List<SearchResult>? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        var layout = inflater.inflate(R.layout.fragment_youtube, container, false)
        YTListRecyclerView = layout.findViewById(R.id.YTListRecyclerView)


        // Get query from user
        var query = "운동"

        try {
            var searchResponse = runBlocking {
                Log.d(TAG, "layout.context: ${layout.context}")
                Log.d(TAG, "parentFragment?.context: ${parentFragment?.context}")
                Log.d(TAG, "context: ${context}")
                Log.d(TAG, "coroutineContext: ${coroutineContext}")
                YTSearchList = YouTubeSearchList(layout.context)

                YTSearchList.loadYouTubeSearchItems(query = query).await()
            }
            Log.d(TAG, "isEmpty: ${searchResponse.isEmpty()}")

            searchResultList = searchResponse.items

            if (searchResultList != null) {
                YTSearchList.printSearchResult(searchResultList!!.iterator(), query)
            }
        } catch (e: GoogleJsonResponseException) {
            Log.e(TAG, "[service error]: ${e.details.code} : ${e.details.message}")
        } catch (e: IOException) {
            Log.e(TAG, "[IO error]: ${e.cause} : ${e.message}")
        } catch (t: Throwable) {
            Log.e(TAG, "[Throwed error]: ${t.message}")
            t.printStackTrace()
        }


        YTListRecyclerView.layoutManager = LinearLayoutManager(context)
        YTListRecyclerView.adapter = YouTubeRecyclerAdapter(searchResultList)

        return layout
    }

    companion object {
        fun newInstance() = YoutubeFragment()
    }
}