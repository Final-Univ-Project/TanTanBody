package hs.capstone.tantanbody.model

import android.app.Application
import android.app.Service
import hs.capstone.tantanbody.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class TTBApplication : Application() {
    val appScope = CoroutineScope(SupervisorJob())
    lateinit var nwCallback: NetworkCallback

    // DB 인스턴스 생성
    val database by lazy { TTBDatabase.getDatabase(this, appScope) }

    // Repository 인스턴스 생성
    val userRepository by lazy { UserRepository() }
    val youtubeRepository by lazy { YouTubeRepository(userRepository.userDto!!.userEmail) }
    val mealRepository by lazy {}

    override fun onCreate() {
        super.onCreate()
        nwCallback = NetworkCallback(this.applicationContext)
    }
    override fun onTerminate() {
        super.onTerminate()
        nwCallback.disableTracking()
    }
}