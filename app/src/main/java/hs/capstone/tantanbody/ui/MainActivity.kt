package hs.capstone.tantanbody.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.google.android.material.bottomnavigation.BottomNavigationView
import hs.capstone.tantanbody.R
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
                    getUsers() //front<->back 통신 test
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

    val data1 = MutableLiveData<UserDto>()
    fun getUsers(): MutableLiveData<UserDto>{
        val call = RetrofitClient.myTestClientService
        call.getUsers("http://192.168.10.27:8080/users").enqueue(object : Callback<UserDto> {
            override fun onResponse(call: Call<UserDto>, response: Response<UserDto>) {

            }
            override fun onFailure(call: Call<UserDto>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
        return data1
    }
}