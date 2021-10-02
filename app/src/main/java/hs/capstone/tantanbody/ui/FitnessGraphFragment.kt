package hs.capstone.tantanbody.user

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import hs.capstone.tantanbody.R

class FitnessGraphFragment : Fragment() {
    val TAG = "FitnessGraphFragment"
    lateinit var barChart: BarChart

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_fitness_graph, container, false)
        barChart = layout.findViewById(R.id.barChart)


        // BarChart 데이터 가져오기
        var workouts = mapOf(
            0 to 23f,
            1 to 20f,
            2 to 30f,
            3 to 21f,
            4 to 16f,
            5 to 26f,
            6 to 18f
        )
//        var weekday = listOf("월", "화", "수", "목", "금", "토", "일")

        var entries = ArrayList<BarEntry>()
        workouts.forEach {
            entries.add(BarEntry(it.key.toFloat(), it.value))
        }

//        var weekdayList = ArrayList<BarEntry>()
//        for (wd in weekday) {
//            weekdayList.add(BarEntry(wd))
//        }

        // BarChart 설정하기
        var DSbar = BarDataSet(entries, getString(R.string.graph_label_fitness))
        DSbar.color = getColorFrom(R.color.orange)
        DSbar.valueTextColor = getColorFrom(R.color.unused_content)

        barChart.xAxis.isEnabled = false
        barChart.axisRight.isEnabled = false
        barChart.description.text = "분(minute)"
        barChart.data = BarData(DSbar)
        barChart.invalidate() // refresh

        return layout
    }

    @SuppressLint("ResourceType")
    fun getColorFrom(address: Int): Int {
        return Color.parseColor(getString(address))
    }

    companion object {
        fun newInstance() = FitnessGraphFragment()
    }
}