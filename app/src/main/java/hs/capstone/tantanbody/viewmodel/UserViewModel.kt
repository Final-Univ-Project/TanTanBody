package hs.capstone.tantanbody.user

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import hs.capstone.tantanbody.model.UserRepository
import hs.capstone.tantanbody.model.data.GoogleAccount
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import java.time.LocalDate
import java.time.LocalDateTime

class UserViewModel(private val repo: UserRepository) : ViewModel() {
    val TAG = "RecordedViewModel"
    lateinit var enterTime : LocalDateTime // 필요에 따라 나눠써야해
    var workoutTimes: List<Int> = repo.workoutTimes // LiveData<> = as LiveData
    var userWeights: List<Int> = repo.userWeights

    // 서버에서 현재 년월일 데이터 가져올 수 있나?
    val today: String = "2021년 10월 3일" // setToday()

    val LoginUser: GoogleAccount?
        get() = repo.googleLoginUser
    var goal: String ?= null

    @RequiresApi(Build.VERSION_CODES.O)
    fun setToday(): String {
        // Q. 코루틴에서 앱 시작/종료 알 수 있나?
        enterTime = LocalDateTime.now() // 년월일 시간분
        Log.d(TAG, "enterTime: ${enterTime}")
        return enterTime.toString()
    }

    fun insertWorkoutTime(minute: Int) = viewModelScope.launch {
        // 오늘날짜(application에 있음) 랑 같이 전달
        repo.insertWorkoutTime(enterTime, minute) // 분(minute)으로 변환
    }
    fun insertWeight(kg: Int) = viewModelScope.launch {
        repo.insertWeight(enterTime, kg)
    }

    fun getWorkoutTimes() : Map<Int, Float> {
        var minutes = mutableMapOf<Int, Float>()
        for (i in 0..6) { // key값= 월,화 바뀌면 수정
            // 맨 마지막부터 하나씩 가져와?
            // workoutTimes.value?.lastIndexOf(6-i)?.toFloat() ?: 0f
            val value = workoutTimes[i].toFloat() ?: 0f
            minutes.put(i, value)
        }
        return minutes
    }
    fun getWeights() : Map<Int, Float> {
        var kgs = mutableMapOf<Int, Float>()
        for (i in 0..6) { // key값= 월,화 바뀌면 수정
            // 맨 마지막부터 하나씩 가져와?
            val value = userWeights[i].toFloat() ?: 0f
            kgs.put(i, value)
        }
        return kgs
    }
}

class UserViewModelFactory(private val repo: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(repo) as T
        }
        throw IllegalArgumentException("알 수 없는 ViewModel 클래스")
    }
}