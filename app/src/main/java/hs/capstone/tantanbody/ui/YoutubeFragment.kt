package hs.capstone.tantanbody.ui

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.api.services.youtube.model.SearchResult
import hs.capstone.tantanbody.R
import hs.capstone.tantanbody.model.TTBApplication
import hs.capstone.tantanbody.model.YouTubeRecyclerAdapter
import hs.capstone.tantanbody.viewmodel.YouTubeViewModel
import hs.capstone.tantanbody.viewmodel.YouTubeViewModelFactory

class YoutubeFragment : Fragment() {
    val TAG = "YoutubeFragment"
    lateinit var YTListRecyclerView: RecyclerView
    var YTList: List<SearchResult> ?= null

    private val youtubeVM by viewModels<YouTubeViewModel> {
        YouTubeViewModelFactory((app as TTBApplication).youtubeRepository)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        var layout = inflater.inflate(R.layout.fragment_youtube, container, false)
        YTListRecyclerView = layout.findViewById(R.id.YTListRecyclerView)

        YTList = youtubeVM.getYouTubeSearchItems(apiKey = getString(R.string.youtube_api_key))
        if (YTList == null) {
            Toast.makeText(context, getString(R.string.fail_loading_youtube), Toast.LENGTH_LONG)
        }
        YTListRecyclerView.layoutManager = LinearLayoutManager(context)
        YTListRecyclerView.adapter = YouTubeRecyclerAdapter(YTList)

        return layout
    }

    companion object {
        lateinit var app: Application
        fun newInstance(app: Application): Fragment {
            this.app = app
            return YoutubeFragment()
        }
    }
}