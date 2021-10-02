package hs.capstone.tantanbody.model.data

import com.google.api.client.util.DateTime

// YouTube 영상
data class YouTubeVideo(
    val videoId: String,
    val publishedAt: DateTime?,
    val channelId: String?,
    val title: String,
    val description: String?,
    val thumbnail: String,
    val channelTitle: String?,
    var isFaverite: Boolean? = false
) {
    override fun toString(): String {
        return "  videoId: ${videoId}\n" +
                " publishedAt: ${publishedAt}\n" +
                " channelId: ${channelId}\n" +
                " title: ${title}\n" +
                " description: ${description}\n" +
                " thumbnail: ${thumbnail}\n" +
                " channelTitle: ${channelTitle}\n"
    }
}
