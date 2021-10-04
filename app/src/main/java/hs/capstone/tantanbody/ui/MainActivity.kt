package hs.capstone.tantanbody.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.google.android.material.bottomnavigation.BottomNavigationView
import hs.capstone.tantanbody.R
import hs.capstone.tantanbody.model.data.DietDto
import hs.capstone.tantanbody.model.data.FoodDto
import hs.capstone.tantanbody.model.data.RecentFoodDto
import hs.capstone.tantanbody.model.data.UserDto
import hs.capstone.tantanbody.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"
    lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigation = findViewById(R.id.bottomNavigation)

        // 메인 Fragment 로드하기
        loadMainFragment(TabRecordFragment.newInstance(application))

        // 하단 네비게이션 선택하면
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.tab_record -> {
                    loadMainFragment(TabRecordFragment.newInstance(application))
                    Log.d(TAG, "R.id.tab_record 클릭")
                    // TODO 해당 메소드들을 정확히 어디에 넣어야 할지 모르겠어서 일단 여기에 몰았습니다.
                    getUsers() //front<->back 통신 test
                    getDietList() //사용자가 기록했던 식단들 가져옴
                    getFoodList() //음식 검색 list 가져옴
                    getRecentFoods() //최근 검색했던 음식 명 가져옴 - 이걸로 추가 검색도 가능하게?
                    true
                }
                R.id.tab_workout -> {
                    loadMainFragment(YoutubeFragment.newInstance())
                    Log.d(TAG, "R.id.tab_workout 클릭")
                    true
                }
                R.id.tab_challenge -> {
                    Log.d(TAG, "R.id.tab_challenge 클릭")
                    true
                }
                R.id.tab_meal -> {
                    Log.d(TAG, "R.id.tab_meal 클릭")
                    true
                }
                R.id.tab_setting -> {
                    Log.d(TAG, "R.id.tab_setting 클릭")
                    true
                }
                else -> false
            }
        }
    }

    fun loadMainFragment(fragment: Fragment) {
        var fragTranser = supportFragmentManager.beginTransaction()
        fragTranser.replace(R.id.mainFragment, fragment)
        fragTranser.addToBackStack(null)
        fragTranser.commit()
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