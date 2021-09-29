package hs.capstone.tantanbody.model

import android.app.Application
import hs.capstone.tantanbody.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class TTBApplication : Application() {
    val appScope = CoroutineScope(SupervisorJob())

    // DB 인스턴스 생성
    val database by lazy { TTBDatabase.getDatabase(this, appScope) }

    // Repository 인스턴스 생성
    val userRepository by lazy { UserRepository() }
    val youtubeRepository by lazy { YouTubeRepository() }
    val mealRepository by lazy {}
}