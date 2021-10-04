package hs.capstone.tantanbody.ui

import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.api.services.youtube.model.SearchResult
import hs.capstone.tantanbody.R
import hs.capstone.tantanbody.model.TTBApplication
import hs.capstone.tantanbody.model.YouTubeRecyclerAdapter
import hs.capstone.tantanbody.model.data.YouTubeVideo
import hs.capstone.tantanbody.viewmodel.YouTubeViewModel
import hs.capstone.tantanbody.viewmodel.YouTubeViewModelFactory

class TabYoutubeFragment : Fragment() {
    val TAG = "YoutubeFragment"
    lateinit var app: Application
    lateinit var YTListRecyclerView: RecyclerView
    var YTList = mutableListOf<YouTubeVideo>()

    private val youtubeViewModel by viewModels<YouTubeViewModel> {
        YouTubeViewModelFactory((app as TTBApplication).youtubeRepository)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        var layout = inflater.inflate(R.layout.fragment_youtube, container, false)
        YTListRecyclerView = layout.findViewById(R.id.YTListRecyclerView)

        youtubeViewModel.loadYouTubeSearchItems(apiKey = getString(R.string.youtube_api_key)).let {
            if (it == null) {
                Toast.makeText(this.context, getString(R.string.fail_loading_youtube), Toast.LENGTH_LONG)
            } else {
                YTList.addAll(it)
            }
        }

        YTListRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@TabYoutubeFragment.context)
            adapter = YouTubeRecyclerAdapter(YTList) { video ->
                Log.d(TAG, "videoId: ${video.videoId} title: ${video.title}")
                buildSettingFavDialog(video.videoId, video.isFaverite).show()
            }
        }

        return layout
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        this.app = context.applicationContext as Application
    }

    fun buildSettingFavDialog(videoId: String, isFav: Boolean): AlertDialog.Builder {
        val message = run {
            if (isFav) "즐겨찾기에서 삭제할까요?"
            else "즐겨찾기에 추가할까요?"
        }

        val favDlg: AlertDialog.Builder = AlertDialog.Builder(
            this.context,
            android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth
        )
        favDlg.setTitle(message)
        favDlg.setNegativeButton("취소", null)
        favDlg.setPositiveButton(
            "확인",
            DialogInterface.OnClickListener { dialog, which ->
                // ViewModel에 업데이트
                youtubeViewModel.changeYoutubeVideoFav(videoId)
            }
        )
        return favDlg
    }

    companion object {
        fun newInstance(): Fragment {
            return TabYoutubeFragment()
        }
    }
}