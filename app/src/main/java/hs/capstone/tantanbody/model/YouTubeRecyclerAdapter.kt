package hs.capstone.tantanbody.model

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import hs.capstone.tantanbody.R
import hs.capstone.tantanbody.model.data.YouTubeVideo

class YouTubeRecyclerAdapter(val items: List<YouTubeVideo>,
                             private val clickListener: (video: YouTubeVideo) -> Unit,
                             private val longClickListener: (video: YouTubeVideo) -> Unit)
    : RecyclerView.Adapter<YouTubeRecyclerAdapter.YouTubeHolder>() {

    inner class YouTubeHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {
        val TAG = "YouTubeHolder"
        // (ui.xml ~~~ data.kt) 이어주기
        var isFavView = itemView.findViewById<ImageView>(R.id.isFavView)
        var keywords = itemView.findViewById<TextView>(R.id.keywords)
        var thumbnailView = itemView.findViewById<ImageView>(R.id.thumbnailView)
        var titleView = itemView.findViewById<TextView>(R.id.titleView)
        var channelTitleView = itemView.findViewById<TextView>(R.id.channelTitleView)

        fun bind(video: YouTubeVideo) {
            if (video.isFaverite) {
                isFavView.visibility = View.VISIBLE
            }
            keywords.text = video.keywords

            titleView.text = video.title
            channelTitleView.text = video.channelTitle
            Picasso.get().load(Uri.parse(video.thumbnail)).into(thumbnailView)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YouTubeHolder {
        val layout = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_youtube_video, parent, false)
        val viewHolder = YouTubeHolder(layout)

        layout.setOnClickListener {
            clickListener.invoke(items[viewHolder.adapterPosition])
        }
        layout.setOnLongClickListener {
            longClickListener.invoke(items[viewHolder.adapterPosition])
            return@setOnLongClickListener true
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: YouTubeHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }
}