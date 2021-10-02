package hs.capstone.tantanbody.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import hs.capstone.tantanbody.R
import hs.capstone.tantanbody.model.TTBApplication
import java.time.LocalDateTime

class WeightAddActivity : AppCompatActivity() {
    val TAG = "WeightAddActivity"
    lateinit var weightAddDay: TextView
    lateinit var weightAddPicker: NumberPicker
    lateinit var setWeightBtn: Button

    private val recordedViewModel by viewModels<UserViewModel> {
        UserViewModelFactory((application as TTBApplication).userRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weight_add)
        weightAddDay = findViewById(R.id.weightAddDay)
        weightAddPicker = findViewById(R.id.weightAddPicker)
        setWeightBtn = findViewById(R.id.setWeightBtn)

        weightAddDay.text = recordedViewModel.today //오늘날짜
        weightAddPicker.maxValue = 10000 //kg
        weightAddPicker.minValue = 1
        weightAddPicker.value = 50
        weightAddPicker.wrapSelectorWheel = false

        weightAddPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            Log.d(TAG, "oldVal: ${oldVal} newVal: ${newVal}")
        }

        setWeightBtn.setOnClickListener {
            Log.d(TAG, "weightPicker.value: ${weightAddPicker.value}")
            Log.d(TAG, "weightPicker.display: ${weightAddPicker.display}")

            //
        }

        loadWeightGraphFragment(WeightGraphFragment.newInstance(recordedViewModel.getWeights()))
    }

    fun loadWeightGraphFragment(fragment: Fragment) {
        var fragTranser = supportFragmentManager.beginTransaction()
        fragTranser.replace(R.id.weightAddGraph, fragment)
        fragTranser.addToBackStack(null)
        fragTranser.commit()
    }
}