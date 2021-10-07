package hs.capstone.tantanbody.user

import androidx.lifecycle.*
import hs.capstone.tantanbody.model.UserRepository
import hs.capstone.tantanbody.model.data.UserDto
import java.lang.IllegalArgumentException
import java.text.SimpleDateFormat
import java.util.*

class UserViewModel(val repo: UserRepository) : ViewModel() {
    val TAG = "UserViewModel"
    val loginUser: UserDto? = repo.userDto
    var goal: LiveData<String> = repo.goal
    var exerciseTimes: LiveData<Map<String, Int>> = repo.exerciseTimes
    var userWeights: LiveData<Map<String, Float>> = repo.userWeights

    var today = run<String> {
        // 참고: https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html
        val todayFormat = SimpleDateFormat("yyyy/MM/dd W'주차' E", Locale.KOREA)
        todayFormat.format(Date()) //"2021년 10월 3일"
    }
    init {
    //        repo.exerciseTimes.observeForever(Observer {
//            this.exerciseTimes = liveData { emit(it) }
//        })
//        repo.userWeights.observeForever(Observer {
//            this.userWeights = liveData { emit(it) }
//        })
    }


//    @RequiresApi(Build.VERSION_CODES.O)
//    fun setToday(): String {
//        // Q. 코루틴에서 앱 시작/종료 알 수 있나?
//        enterTime = LocalDateTime.now() // 년월일 시간분
//        Log.d(TAG, "enterTime: ${enterTime}")
//        return enterTime.toString()
//    }

    fun setGoal(goal: String) {
        repo.setGoal(goal)
    }
    fun insertExerciseTime(minute: Int) {
        // 오늘날짜(application에 있음) 랑 같이 전달
        repo.insertExerciseTime(today, minute) // 분(minute)으로 변환
    }
    fun insertWeight(kg: Float) {
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