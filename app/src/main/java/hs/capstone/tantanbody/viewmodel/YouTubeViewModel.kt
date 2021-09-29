package hs.capstone.tantanbody.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.api.services.youtube.model.SearchResult
import hs.capstone.tantanbody.model.YouTubeRepository
import java.lang.IllegalArgumentException

class YouTubeViewModel(private val repo: YouTubeRepository) : ViewModel() {
    val TAG = "YouTubeViewModel"

    fun getYouTubeSearchItems(apiKey: String): List<SearchResult>? {
        return repo.loadYouTubeSearchItems(apiKey)
    }
}

class YouTubeViewModelFactory(private val repo: YouTubeRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(YouTubeViewModel::class.java)) {
            return YouTubeViewModel(repo) as T
        }
        throw IllegalArgumentException("알 수 없는 ViewModel 클래스")
    }
}