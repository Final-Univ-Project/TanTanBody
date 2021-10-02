package hs.capstone.tantanbody.model

import com.google.api.services.youtube.model.SearchResult
import com.google.api.services.youtube.model.Thumbnail
import hs.capstone.tantanbody.model.data.YouTubeVideo

class YouTubeRepository {
    val TAG = "YouTubeRepository"
    var favoriteYoutubeList = mutableListOf<YouTubeVideo>()

    // List<SearchResult>를 List<YouTubeVideo>로 변환하는 메소드
    fun insertFavoriteYoutubeVideo(searchResult: SearchResult) {
//        while (searchResults.hasNext()) {
        val signleVideo = searchResult //.next()
        val thumbnail = signleVideo.snippet.thumbnails["default"] as Thumbnail?

        val video = YouTubeVideo(
            videoId = signleVideo.id.videoId,
            publishedAt = signleVideo.snippet.publishedAt,
            channelId = signleVideo.snippet.channelId,
            title = signleVideo.snippet.title,
            description = signleVideo.snippet.description,
            thumbnail = thumbnail!!.url,
            channelTitle = signleVideo.snippet.channelTitle
        )
        favoriteYoutubeList.add(video)
    }


}