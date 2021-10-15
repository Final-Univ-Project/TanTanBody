package hs.capstone.tantanbody.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import hs.capstone.tantanbody.model.data.*
import hs.capstone.tantanbody.model.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class UserRepository {
    val TAG = "UserRepository"
    var userDto: UserDto ?= null
    var goal: MutableLiveData<String> = loadGoal() as MutableLiveData<String>
    var exerciseTimes: MutableLiveData<MutableMap<String, Int>> =
        loadExerciseTimes() as MutableLiveData<MutableMap<String, Int>>
    var userWeights: MutableLiveData<MutableMap<String, Float>> =
        loadUserWeights() as MutableLiveData<MutableMap<String, Float>>

    fun loadGoal() = liveData<String> {
        ""
    }
    fun setGoal(goal: String) {
        this.goal.value = goal
    }
    fun deleteGoal() {
        this.goal.value = ""
    }

    fun getWeekdayByDate(date: Long): String {
        return SimpleDateFormat("E", Locale.KOREA).format(Date(date))
    }
    fun loadExerciseTimes() = liveData<MutableMap<String, Int>> { // check!!!!!
        val dayMillies = 86400000
        // 월~일 고정된 크기의 이번주 운동시간
        emit(mutableMapOf<String, Int>(
            getWeekdayByDate(Date(Date().time - dayMillies*6).time) to 23,
            getWeekdayByDate(Date(Date().time - dayMillies*5).time) to 20,
            getWeekdayByDate(Date(Date().time - dayMillies*4).time) to 30,
            getWeekdayByDate(Date(Date().time - dayMillies*3).time) to 21,
            getWeekdayByDate(Date(Date().time - dayMillies*2).time) to 16,
            getWeekdayByDate(Date(Date().time - dayMillies).time) to 26,
            getWeekdayByDate(Date().time) to 18
        ) ?: mutableMapOf())
        Log.e(TAG, "exerciseTimes: ${exerciseTimes.value}")
    }
    fun insertExerciseTime(date: Date, minute: Int) {
        this.exerciseTimes.value
        // 같은 날짜면 업데이트, 없으면 추가
        Log.d(TAG, "date: ${date}, minute: ${minute}")
    }

    fun loadUserWeights() = liveData<MutableMap<String, Float>> {
        val dayMillies = 86400000
        emit(mutableMapOf<String, Float>(
            getWeekdayByDate(Date(Date().time - dayMillies*4).time) to 45f,
            getWeekdayByDate(Date(Date().time - dayMillies*3).time) to 52f,
            getWeekdayByDate(Date(Date().time - dayMillies*2).time) to 54.8f,
            getWeekdayByDate(Date(Date().time - dayMillies).time) to 48f
        ) ?: mutableMapOf())
        Log.e(TAG, "exerciseTimes: ${userWeights.value}")
    }
    fun insertWeight(date: Date, kg: Float) {
        userWeights.value?.put(getWeekdayByDate(date.time), kg)
        Log.d(TAG, "date: ${date.time}, kg: ${kg.toDouble()}")
    }

    fun checkIsSignedUser(user: UserDto) {
        userDto = user

        if (saveUserData(user)) { // 등록O or 새로 등록O
            Log.e(TAG, "사용자 등록 서버통신이 이루어졌습니다!")
        } else { // NW오류
            Log.e(TAG, "서버통신 중에 오류가 발견되었습니다!")
        }
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
                Log.d("saveUserData 결과", "성공 : ${response.raw()}")
                Log.d("가져온 데이터", "${response.body()}")
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d("saveUserData 결과", "실패 : $t")
                isCompleted = false
            }
        })
        return isCompleted
    }

    val data1 = MutableLiveData<UserDto>()
    fun getUsers(): MutableLiveData<UserDto>{
        val call = RetrofitClient.myTestClientService
        call.getUsers().enqueue(object : Callback<ArrayList<UserDto>> {
            override fun onResponse(call: Call<ArrayList<UserDto>>, response: Response<ArrayList<UserDto>>) {
                Log.d("get Users 결과", "성공 : ${response.raw()}")
                Log.d("가져온 데이터", "${response.body()}")
            }
            override fun onFailure(call: Call<ArrayList<UserDto>>, t: Throwable) {
                Log.d("get Users 결과", "실패 : $t")
            }
        })
        return data1
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