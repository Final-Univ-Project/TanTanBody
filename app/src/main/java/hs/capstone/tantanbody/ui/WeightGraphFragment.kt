package hs.capstone.tantanbody.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.*
import hs.capstone.tantanbody.R
import kotlin.collections.ArrayList

class WeightGraphFragment : Fragment() {
    val TAG = "WeightGraphFragment"
    lateinit var lineChart: LineChart

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val layout = inflater.inflate(R.layout.fragment_weight_graph, container, false)
        lineChart = layout.findViewById(R.id.lineChart)

        // LineChart 데이터 가져오기
        var weights = mutableListOf(23f, 20f, 30f, 21f, 16f, 26f, 18f)
        var weightList = ArrayList<Entry>()
        for (i in 0..6) {
            weightList.add(Entry(weights.get(i), i.toFloat()))
        }

//        var weekday = listOf("월", "화", "수", "목", "금", "토", "일")
//        var weekdayList = ArrayList<String>()
//        for (wd in weekday) {
//            weekdayList.add(wd)
//        }

        // BarChart 설정하기
        var weightDS = LineDataSet(weightList, "몸무게 변화량")
        lineChart.animateY(100)
        lineChart.data = LineData(weightDS)

        return layout
    }

    companion object {
        fun newInstance(weights: FloatArray) = WeightGraphFragment()
    }
}