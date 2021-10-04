package hs.capstone.tantanbody.user

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import hs.capstone.tantanbody.model.UserRepository
import hs.capstone.tantanbody.model.data.GoogleAccount
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import java.time.LocalDate
import java.time.LocalDateTime

class UserViewModel(private val repo: UserRepository) : ViewModel() {
    val TAG = "RecordedViewModel"
    val LoginUser: GoogleAccount? = repo.googleLoginUser
    var goal: LiveData<String> = repo.goal
    var exerciseTimes: LiveData<Map<String, Int>> = repo.exerciseTimes
    var userWeights: LiveData<Map<String, Float>> = repo.userWeights

    val today: String = "2021년 10월 3일"


//    @RequiresApi(Build.VERSION_CODES.O)
//    fun setToday(): String {
//        // Q. 코루틴에서 앱 시작/종료 알 수 있나?
//        enterTime = LocalDateTime.now() // 년월일 시간분
//        Log.d(TAG, "enterTime: ${enterTime}")
//        return enterTime.toString()
//    }

    fun insertExerciseTime(minute: Int) = viewModelScope.launch {
        // 오늘날짜(application에 있음) 랑 같이 전달
        repo.insertExerciseTime(today, minute) // 분(minute)으로 변환
    }
    fun insertWeight(kg: Float) = viewModelScope.launch {
        repo.insertWeight(today, kg)
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