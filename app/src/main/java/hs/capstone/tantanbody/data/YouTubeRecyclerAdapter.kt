package hs.capstone.tantanbody.data

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.api.services.youtube.model.SearchResult
import com.google.api.services.youtube.model.Thumbnail
import com.google.api.services.youtube.model.ThumbnailDetails
import com.squareup.picasso.Picasso
import hs.capstone.tantanbody.R

class YouTubeRecyclerAdapter(val searchResultList: List<SearchResult>?)
    : RecyclerView.Adapter<YouTubeRecyclerAdapter.YouTubeHolder>() {

    inner class YouTubeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // (ui.xml ~~~ data.kt) 이어주기
        var thumbnailView = itemView.findViewById<ImageView>(R.id.thumbnailView)
        var titleView = itemView.findViewById<TextView>(R.id.titleView)
        var channelTitleView = itemView.findViewById<TextView>(R.id.channelTitleView)

        fun bind(YTvideo: YouTubeVideo) {
            Picasso.get().load(Uri.parse(YTvideo.thumbnail)).into(thumbnailView)
//            thumbnailView.setImageURI(Uri.parse(YTvideo.thumbnail))
            titleView.text = YTvideo.title
            channelTitleView.text = YTvideo.channelTitle
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YouTubeHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.item_youtube_video, parent, false)
        return YouTubeHolder(layout)
    }

    override fun onBindViewHolder(holder: YouTubeHolder, position: Int) {
        searchResultList?.also {
            val YTvideo = it.get(position).run {
                val thumbnail = this.snippet.thumbnails["default"] as Thumbnail
                YouTubeVideo(
                    videoId = "${this.id}",
                    publishedAt = this.snippet.publishedAt,
                    channelId = this.snippet.channelId,
                    title = this.snippet.title,
                    description = this.snippet.description,
                    thumbnail = thumbnail.url,
                    channelTitle = this.snippet.channelTitle
                )
            }
            holder.bind(YTvideo)
        }
    }

    override fun getItemCount(): Int {
        return 5
    }
}