package hs.capstone.tantanbody.user

import android.util.Log
import androidx.lifecycle.*
import hs.capstone.tantanbody.model.UserRepository
import hs.capstone.tantanbody.model.data.UserDto
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import java.lang.IllegalArgumentException
import java.text.SimpleDateFormat
import java.util.*

class UserViewModel(val repo: UserRepository) : ViewModel() {
    val TAG = "UserViewModel"
    val loginUser: UserDto? = repo.userDto
    var goal: LiveData<String> = repo.goal
    var exerciseTimes: MutableLiveData<MutableMap<String, Int>> = repo.exerciseTimes
    var userWeights: MutableLiveData<MutableMap<String, Float>> = repo.userWeights


    fun getWeekOfMonth(date: Long): String {
        return SimpleDateFormat("M'월' W'주차'", Locale.KOREA).format(Date(date))
    }

    fun setGoal(goal: String) {
        repo.setGoal(goal)
    }
    fun insertExerciseTime(minute: Int) {
        repo.insertExerciseTime(Date(), minute)
    }
    fun insertWeight(kg: Float) {
        repo.insertWeight(Date(), kg)
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