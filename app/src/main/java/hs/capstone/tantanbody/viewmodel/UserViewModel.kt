package hs.capstone.tantanbody.user

import androidx.lifecycle.*
import androidx.lifecycle.Observer
import hs.capstone.tantanbody.model.UserRepository
import hs.capstone.tantanbody.model.data.UserDto
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import java.text.SimpleDateFormat
import java.util.*

class UserViewModel(val repo: UserRepository) : ViewModel() {
    val TAG = "UserViewModel"
    val loginUser: UserDto? = repo.userDto
    var goal: MutableLiveData<String> = repo.goal
    var exerciseTimes: MutableLiveData<MutableMap<String, Int>> = repo.exerciseTimes
    var userWeights: MutableLiveData<MutableMap<String, Float>> = repo.userWeights

    // 뷰모델에서 liveData observe하기
    val obrGoal = Observer<String> { goal.value = it }
    val obrMinutes = Observer<MutableMap<String, Int>> { exerciseTimes.value = it }
    val obrKgs = Observer<MutableMap<String, Float>> { userWeights.value = it }
    init {
        repo.goal.observeForever(obrGoal)
        repo.exerciseTimes.observeForever(obrMinutes)
        repo.userWeights.observeForever(obrKgs)
    }
    override fun onCleared() {
        repo.goal.removeObserver(obrGoal)
        repo.exerciseTimes.removeObserver(obrMinutes)
        super.onCleared()
    }

    fun getWeekOfMonth(date: Long): String {
        return SimpleDateFormat("M'월' W'주차'", Locale.KOREA).format(Date(date))
    }

    fun setGoal(goal: String) = viewModelScope.launch {
        repo.setGoal(goal)
    }
    fun insertExerciseTime(minute: Int) = viewModelScope.launch {
        repo.insertExerciseTime(Date(), minute)
    }
    fun insertWeight(kg: Float) = viewModelScope.launch {
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