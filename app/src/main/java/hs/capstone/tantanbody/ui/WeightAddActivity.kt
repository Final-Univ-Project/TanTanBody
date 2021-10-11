package hs.capstone.tantanbody.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import hs.capstone.tantanbody.R
import hs.capstone.tantanbody.model.TTBApplication
import hs.capstone.tantanbody.ui.MainActivity
import java.time.LocalDateTime
import java.util.*

class WeightAddActivity : AppCompatActivity() {
    val TAG = "WeightAddActivity"
    lateinit var weightAddDay: TextView
    lateinit var weightPicker: NumberPicker
    lateinit var weightDecimalPicker: NumberPicker
    lateinit var setWeightBtn: Button

    private val model by viewModels<UserViewModel> {
        UserViewModelFactory((application as TTBApplication).userRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weight_add)
        weightAddDay = findViewById(R.id.weightAddDay)
        weightPicker = findViewById(R.id.weightPicker)
        weightDecimalPicker = findViewById(R.id.weightDecimalPicker)
        setWeightBtn = findViewById(R.id.setWeightBtn)

        loadWeightGraphFragment(
            WeightGraphFragment.newInstance(model.userWeights.value ?: mapOf()))

        weightAddDay.text = model.getWeekOfMonth(Date().time) //오늘날짜
        weightPicker.minValue = 20
        weightPicker.maxValue = 300 //kg
        weightPicker.value = 50
        weightPicker.wrapSelectorWheel = false
        weightDecimalPicker.minValue = 0
        weightDecimalPicker.maxValue = 9
        weightDecimalPicker.value = 0
        weightDecimalPicker.wrapSelectorWheel = false

        setWeightBtn.setOnClickListener {
            val weight = "${weightPicker.value}.${weightDecimalPicker.value}".toFloat()
            Log.d(TAG, "weightPicker: ${weightPicker.value}")
            Log.d(TAG, "weightDecimalPicker: ${weightDecimalPicker.value}")
            Log.d(TAG, "weight: ${weight}")

            model.insertWeight(weight)
            val intent = Intent(baseContext, MainActivity::class.java)
            startActivity(intent)
        }
    }

    fun loadWeightGraphFragment(fragment: Fragment) {
        var fragTranser = supportFragmentManager.beginTransaction()
        fragTranser.replace(R.id.weightAddGraph, fragment)
        fragTranser.addToBackStack(null)
        fragTranser.commit()
    }
}