package hs.capstone.tantanbody.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import hs.capstone.tantanbody.model.data.GoogleAccount
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.time.LocalDateTime

class UserRepository {
    val TAG = "UserRepository"
    var googleLoginUser: GoogleAccount ?= null
    var goal: MutableLiveData<String> = loadGoal() as MutableLiveData<String>
    var exerciseTimes: MutableLiveData<Map<String, Int>> =
        loadExerciseTimes() as MutableLiveData<Map<String, Int>>
    var userWeights: MutableLiveData<Map<String, Float>> =
        loadUserWeights() as MutableLiveData<Map<String, Float>>

    fun loadGoal() = liveData<String> {
        ""
    }
    fun loadExerciseTimes() = liveData<Map<String, Int>> {
        mapOf<String, Int>(
            "월" to 23,
            "화" to 20,
            "수" to 30,
            "목" to 21,
            "금" to 16,
            "토" to 26,
            "일" to 18
        ) ?: mapOf()
    }
    fun loadUserWeights() = liveData<Map<String, Float>> {
        mapOf<String, Float>(
            "월" to 45f,
            "화" to 52f,
            "수" to 54.8f,
            "목" to 48f,
            "금" to 60f,
            "토" to 42f,
            "일" to 40f
        ) ?: mapOf()
    }

    fun insertExerciseTime(date: String, minute: Int) {
        // 같은 날짜면 업데이트, 없으면 추가
        Log.d(TAG, "date: ${date}, minute: ${minute}")
    }
    fun insertWeight(date: String, kg: Float) {
        // 위와 동일
        Log.d(TAG, "date: ${date}, kg: ${kg.toDouble()}")
    }
}