package hs.capstone.tantanbody.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import hs.capstone.tantanbody.model.data.*
import hs.capstone.tantanbody.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository {
    val TAG = "UserRepository"
    var userDto: UserDto ?= null // getUsers()
    var goal: MutableLiveData<String> = loadGoal() as MutableLiveData<String>
    var exerciseTimes: MutableLiveData<Map<String, Int>> =
        loadExerciseTimes() as MutableLiveData<Map<String, Int>>
    var userWeights: MutableLiveData<Map<String, Float>> =
        loadUserWeights() as MutableLiveData<Map<String, Float>>

    fun loadGoal() = liveData<String> {
        ""
    }
    fun loadExerciseTimes() = liveData<Map<String, Int>> {
        // 월~일 고정된 크기의 이번주 운동시간
        emit(mapOf<String, Int>(
            "월" to 23,
            "화" to 20,
            "수" to 30,
            "목" to 21,
            "금" to 16,
            "토" to 26,
            "일" to 18
        ) ?: mapOf())
        Log.e(TAG, "exerciseTimes: ${exerciseTimes.value}")
    }
    fun loadUserWeights() = liveData<Map<String, Float>> {
        emit(mapOf<String, Float>(
            "월" to 45f,
            "화" to 52f,
            "수" to 54.8f,
            "목" to 48f,
            "금" to 60f,
            "토" to 42f,
            "일" to 40f
        ) ?: mapOf())
        Log.e(TAG, "exerciseTimes: ${userWeights.value}")
    }

    fun setGoal(goal: String) {
        this.goal.value = goal
    }
    fun insertExerciseTime(date: String, minute: Int) {
        this.exerciseTimes.value
        // 같은 날짜면 업데이트, 없으면 추가
        Log.d(TAG, "date: ${date}, minute: ${minute}")
    }
    fun insertWeight(date: String, kg: Float) {
        // 위와 동일
        Log.d(TAG, "date: ${date}, kg: ${kg.toDouble()}")
    }


    /**
     * 등록된 사용자의 정보를 가져옴
     *
     * 신규 회원인지 아니면 있는 회원인지 그냥 관례상(?) 확인 하기
     */
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