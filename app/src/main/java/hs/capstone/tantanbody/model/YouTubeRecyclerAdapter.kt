package hs.capstone.tantanbody.model

import android.app.AlertDialog
import android.content.DialogInterface
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.api.services.youtube.model.SearchResult
import com.google.api.services.youtube.model.Thumbnail
import com.squareup.picasso.Picasso
import hs.capstone.tantanbody.R
import hs.capstone.tantanbody.model.data.YouTubeVideo
import kotlin.coroutines.coroutineContext

class YouTubeRecyclerAdapter(private val youtubeVideoList: List<YouTubeVideo>?)
    : RecyclerView.Adapter<YouTubeRecyclerAdapter.YouTubeHolder>() {

    inner class YouTubeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val TAG = "YouTubeHolder"
        // (ui.xml ~~~ data.kt) 이어주기
        var isFavView = itemView.findViewById<ImageView>(R.id.isFavView)
        var thumbnailView = itemView.findViewById<ImageView>(R.id.thumbnailView)
        var titleView = itemView.findViewById<TextView>(R.id.titleView)
        var channelTitleView = itemView.findViewById<TextView>(R.id.channelTitleView)

        fun bind(video: YouTubeVideo) {
            itemView.contentDescription = video.videoId

            Picasso.get().load(Uri.parse(video.thumbnail)).into(thumbnailView)
            titleView.text = video.title
            channelTitleView.text = video.channelTitle
            if (video.isFaverite) {
                isFavView.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YouTubeHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.item_youtube_video, parent, false)
        return YouTubeHolder(layout)
    }

    override fun onBindViewHolder(holder: YouTubeHolder, position: Int) {

        // YouTubeVideo 뷰바인딩
        youtubeVideoList?.also {
            holder.bind(it.get(position))
        }
    }

    override fun getItemCount(): Int {
        return youtubeVideoList?.size ?: 0
    }
}