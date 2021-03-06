package hs.capstone.tantanbody.ui

import android.app.Activity
import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

    lateinit var YTList: MutableList<YouTubeVideo>

    private val model by viewModels<YouTubeViewModel> {
        YouTubeViewModelFactory((app as TTBApplication).youtubeRepository)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        var layout = inflater.inflate(R.layout.fragment_tab_youtube, container, false)
        YTListRecyclerView = layout.findViewById(R.id.YTListRecyclerView)

        model.favYoutubeVideos
        YTList = (model.youtubeVideos.value
            ?: model.loadYouTubeSearchItems(apiKey = getString(R.string.youtube_api_key)).value)!!

        model.youtubeVideos.observe(viewLifecycleOwner, Observer { videos ->
            YTList = videos
        })

        YTListRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = YouTubeRecyclerAdapter(
                YTList,
                // 클릭 리스너 매개변수
                clickListener = { video ->
                    Log.d(TAG, "videoId: ${video.videoId} title: ${video.title}")
                    model.insertClickedYouTube(video = video)

                    // TODO. 클릭하면 운동시작 기록하기 (서버?프론트?)
                    val intent = Intent(context, YoutubeVideoActivity.newInstance(video)::class.java)
                    startActivity(intent)
                },
                // 롱클릭 리스너 매개변수
                longClickListener = { video ->
                    Log.d(TAG, "videoId: ${video.videoId} title: ${video.title}")
                    // 즐겨찾기 dialog 보여줌
                    buildSettingFavDialog(video, video.isFaverite).show()
                }
            )
        }
        return layout
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        this.app = context.applicationContext as Application
    }

    // 즐겨찾기 Dialog창 띄움
    fun buildSettingFavDialog(video: YouTubeVideo, isFav: Boolean): AlertDialog.Builder {
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
                if (isFav) {
                    model.deleteFavYouTubeVideo(video = video)
                } else {
                    model.insertFavYouTubeVideo(video = video)
                }
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