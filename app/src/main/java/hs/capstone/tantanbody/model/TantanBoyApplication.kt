package hs.capstone.tantanbody.model

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class TantanBoyApplication : Application() {
    val appScope = CoroutineScope(SupervisorJob())

    // DB 인스턴스 생성
    val database by lazy { TantanBodyDatabase.getDatabase(this, appScope) }

    // Repository 인스턴스 생성
    val userRepository by lazy { UserRepository() }
    val youtubeRepository by lazy {}
    val mealRepository by lazy {}
}