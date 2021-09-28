package hs.capstone.tantanbody.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import hs.capstone.tantanbody.R

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
            when(item.itemId) {
                R.id.tab_record -> {
                    loadMainFragment(TabRecordFragment.newInstance(application))
                    Log.d(TAG, "R.id.tab_record 클릭")
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
}