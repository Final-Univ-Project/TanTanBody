package hs.capstone.tantanbody.ui

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import hs.capstone.tantanbody.R

class TabRecordFragment : Fragment() {
    val TAG = "TabRecordFragment"
    lateinit var fitnessGraphTitle: TextView
    lateinit var weightGraphTitle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_tab_record, container, false)
        fitnessGraphTitle = layout.findViewById(R.id.fitnessGraphTitle)
        weightGraphTitle = layout.findViewById(R.id.weightGraphTitle)

        fitnessGraphTitle.setOnClickListener {
            loadGraphFragment(FitnessGraphFragment.newInstance())
            fitnessGraphTitle.setTextColor(getColorFrom(R.color.using_content))
            weightGraphTitle.setTextColor(getColorFrom(R.color.unused_content))
            Log.d(TAG, "R.id.recordFitnessGraph 클릭")
        }
        weightGraphTitle.setOnClickListener {
            loadGraphFragment(WeightGraphFragment.newInstance())
            weightGraphTitle.setTextColor(getColorFrom(R.color.using_content))
            fitnessGraphTitle.setTextColor(getColorFrom(R.color.unused_content))
            Log.d(TAG, "R.id.recordWeightGraph 클릭")
        }
        fitnessGraphTitle.performClick()
        return layout
    }

    @SuppressLint("ResourceType")
    fun getColorFrom(address: Int): Int {
        return Color.parseColor(getString(address))
    }

    companion object {
        fun newInstance() = TabRecordFragment()
    }

    fun loadGraphFragment(fragment: Fragment) {
        var fragTranser = parentFragmentManager.beginTransaction()
        fragTranser.replace(R.id.recordGraphFragment, fragment)
        fragTranser.setReorderingAllowed(true)
        fragTranser.commit()
    }
}