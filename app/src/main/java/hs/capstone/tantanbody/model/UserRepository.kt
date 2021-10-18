package hs.capstone.tantanbody.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import hs.capstone.tantanbody.model.data.*
import hs.capstone.tantanbody.model.network.RetrofitClient
import kotlinx.coroutines.flow.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class UserRepository {
    val TAG = "UserRepository"
    var userDto: UserDto ?= null
    lateinit var goal: MutableLiveData<String>
    lateinit var exerciseTimes: MutableLiveData<MutableMap<String, Int>>
    lateinit var userWeights: MutableLiveData<MutableMap<String, Float>>

    // Date날짜 입력하면, 요일 반환
    fun getWeekdayByDate(date: Long): String {
        return SimpleDateFormat("E", Locale.KOREA).format(Date(date))
    }


    // 사용자 등록 및 서버통신 확인
    fun checkIsSignedUser(user: UserDto) {
        // (프론트) 유저 저장
        userDto = user

        // (서버) 유저 저장
        if (saveUserData(user)) { // 등록O or 새로 등록O
            Log.d(TAG, "사용자 등록 서버통신이 이루어졌습니다!")
            loadUserData()
        } else { // NW오류
            Log.e(TAG, "서버통신 중에 오류가 발견되었습니다!")
        }
    }
    fun loadUserData() {
        // (서버) 유저 운동목표,시간,몸무게 가져오기
        loadGoal()
        loadExerciseTimes()
        loadUserWeights()
    }

    // 운동목표 가져오기
    fun loadGoal() {
        goal.value = "init"
    }
    // 운동 목표 설정
    fun setGoal(goal: String) {
        this.goal.value = goal
        // TODO. (서버) 운동목표 추가
    }
    // 운동 목표 삭제
    fun deleteGoal() {
        this.goal.value = ""
        // TODO. (서버) 운동목표 삭제
    }

    // 운동 시간(~일주일) 가져오기
    fun loadExerciseTimes() {
        val dayMillies = 86400000
        // 월~일 고정된 크기의 이번주 운동시간
        exerciseTimes.value = mutableMapOf<String, Int>(
            getWeekdayByDate(Date(Date().time - dayMillies*6).time) to 23,
            getWeekdayByDate(Date(Date().time - dayMillies*5).time) to 20,
            getWeekdayByDate(Date(Date().time - dayMillies*4).time) to 30,
            getWeekdayByDate(Date(Date().time - dayMillies*3).time) to 21,
            getWeekdayByDate(Date(Date().time - dayMillies*2).time) to 16,
            getWeekdayByDate(Date(Date().time - dayMillies).time) to 26,
            getWeekdayByDate(Date().time) to 18
        ) ?: mutableMapOf()
    }
    // 운동한 시간 추가
    suspend fun insertExerciseTime(date: Date, minute: Int) {
        // TODO. (프론트) NUll인 경우 고려

        this.exerciseTimes.value?.put(getWeekdayByDate(date.time), minute)
        // TODO. (서버) 운동시간 추가
        Log.d(TAG, "date: ${date}, minute: ${minute}")
    }

    // 사용자 몸무게 기록(~일주일) 가져오기
    fun loadUserWeights() {
        val dayMillies = 86400000
        userWeights.value = mutableMapOf<String, Float>(
            getWeekdayByDate(Date(Date().time - dayMillies*4).time) to 45f,
            getWeekdayByDate(Date(Date().time - dayMillies*3).time) to 52f,
            getWeekdayByDate(Date(Date().time - dayMillies*2).time) to 54.8f,
            getWeekdayByDate(Date(Date().time - dayMillies).time) to 48f
        ) ?: mutableMapOf()
    }
    // 사용자 몸무게 기록 추가
    suspend fun insertWeight(date: Date, kg: Float) {
        this.userWeights.value?.put(getWeekdayByDate(date.time), kg)
        // TODO. (서버) 몸무게 추가
    }

    /**
     * 등록된 사용자의 정보를 가져옴
     *
     * 신규 회원인지 아니면 있는 회원인지 그냥 관례상(?) 확인 하기
     */
    fun saveUserData(user: UserDto): Boolean {
        val call = RetrofitClient.myTestClientService
        var isCompleted = true
        call.saveUserData(
            userEmail = user.userEmail,
            userName = user.userName,
            userPhoto = user.userPhoto,
            userGoal = user.userGoal
        ).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                Log.d(TAG, "성공 : ${response.raw()}")
                Log.d(TAG, "${response.body()}")
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e(TAG, "실패 : $t")
                isCompleted = false
            }
        })
        return isCompleted
    }

    /**
     * 사용자가 작성했던 식단 리스트를 가져옴
     *
     * 사용자 email을 파라미터로 넘겨야 함
     */
    val data2 = MutableLiveData<DietDto>()
    fun getDietList(): MutableLiveData<DietDto> {
        val call = RetrofitClient.myTestClientService
        call.getFoods("pigismile@gmail.com").enqueue(object : Callback<ArrayList<DietDto>> {
            override fun onResponse(call: Call<ArrayList<DietDto>>, response: Response<ArrayList<DietDto>>) {
                Log.d("get DietList 결과", "성공 : ${response.raw()}")
                Log.d("가져온 데이터", "${response.body()}")

                // TODO userEmail을 파라미터로 넘겨야 합니다. 처음 구글에서 가져온 사용자 메일 정보를 상수에 담고 그 상수를 가지고 계속 사용해야 합니다.
                // TODO 가져온 데이터 가지고 화면에 뿌리면 됩니다.

            }

            override fun onFailure(call: Call<ArrayList<DietDto>>, t: Throwable) {
                // TODO("Not yet implemented")
                Log.d("get DietList 결과", "실패 : $t")
            }
        })
        return data2
    }

    /**
     * 사용자가 검색했던 최근 음식을 가져옴
     *
     * 사용자 email을 파라미터로 넘겨야 함
     */
    val data3 = MutableLiveData<RecentFoodDto>()
    fun getRecentFoods(): MutableLiveData<RecentFoodDto> {
        val call = RetrofitClient.myTestClientService
        call.getRecentFoods("pigismile@gmail.com").enqueue(object : Callback<RecentFoodDto> {
            override fun onResponse(call: Call<RecentFoodDto>, response: Response<RecentFoodDto>) {
                Log.d("get RecentFoods 결과", "성공 : ${response.raw()}")
                Log.d("가져온 데이터", "${response.body()}")

                // TODO 사용자 이메일(userEmail)을 파라미터로 넘겨야 합니다. 처음 구글에서 가져온 사용자 메일 정보를 상수에 담고 그 상수를 가지고 계속 사용해야 합니다.
                // TODO 가져온 데이터 가지고 화면에 뿌리면 됩니다.

            }

            override fun onFailure(call: Call<RecentFoodDto>, t: Throwable) {
                // TODO("Not yet implemented")
                Log.d("get RecentFoods 결과", "실패 : $t")
            }
        })
        return data3
    }

    /**
     * 음식 검색하기
     *
     * 검색어를 파라미터로 넘겨야 함
     */
    val data4 = MutableLiveData<FoodDto>()
    fun getFoodList(): MutableLiveData<FoodDto> {
        val call = RetrofitClient.myTestClientService
        call.getFoodList("메밀").enqueue(object : Callback<ArrayList<FoodDto>> {
            override fun onResponse(call: Call<ArrayList<FoodDto>>, response: Response<ArrayList<FoodDto>>) {
                Log.d("get FoodList 결과", "성공 : ${response.raw()}")
                Log.d("가져온 데이터", "${response.body()}")

                // TODO 검색어(searchFood)를 가져와서 파라미터로 넘겨야 합니다.
                // TODO 가져온 데이터 가지고 화면에 뿌리면 됩니다.

            }

            override fun onFailure(call: Call<ArrayList<FoodDto>>, t: Throwable) {
                // TODO("Not yet implemented")
                Log.d("get FoodList 결과", "실패 : $t")
            }
        })
        return data4
    }
}