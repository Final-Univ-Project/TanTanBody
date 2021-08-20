package hs.capstone.tantanbody.ui

import android.os.Bundle
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
    lateinit var barChart: BarChart

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_fitness_graph, container, false)
        barChart = layout.findViewById(R.id.barChart)


        // BarChart 데이터 가져오기
        var workoutTimes = mutableListOf(23f, 20f, 30f, 21f, 16f, 26f, 18f)
        var workoutTimeList = ArrayList<BarEntry>()
        for (i in 0..6) {
            workoutTimeList.add(BarEntry(workoutTimes[i], i.toFloat()))
        }

//        var weekday = listOf("월", "화", "수", "목", "금", "토", "일")
//        var weekdayList = ArrayList<String>()
//        for (wd in weekday) {
//            weekdayList.add(wd)
//        }

        // BarChart 설정하기
        var workoutDS = BarDataSet(workoutTimeList, "운동시간")
        barChart.animateY(100)
        barChart.data = BarData(workoutDS)

        // 색, round edge, 한글깨짐? 고치기

        return layout
    }

    companion object {
        fun newInstance() = FitnessGraphFragment()
    }
}