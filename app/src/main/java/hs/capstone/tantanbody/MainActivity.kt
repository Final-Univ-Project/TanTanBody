package hs.capstone.tantanbody

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import hs.capstone.tantanbody.ui.FitnessGraphFragment
import hs.capstone.tantanbody.ui.WeightGraphFragment
import hs.capstone.tantanbody.ui.YoutubeFragment

class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"
    lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigation = findViewById(R.id.bottomNavigation)

        // 메인 Fragment 로드하기
        loadMainFragment(FitnessGraphFragment.newInstance())

        // 하단 네비게이션 선택하면
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.page1 -> {
//                    loadMainFragment(FitnessGraphFragment.newInstance())

                    var weights = floatArrayOf(23f, 20f, 30f, 21f, 16f, 26f, 18f)
                    loadMainFragment(WeightGraphFragment.newInstance(weights))

                    Log.d(TAG, "R.id.page1: ${R.id.page1} 선택")
                    true
                }
                R.id.page2 -> {
                    loadMainFragment(YoutubeFragment.newInstance())
                    Log.d(TAG, "R.id.page1: ${R.id.page1} 선택")
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