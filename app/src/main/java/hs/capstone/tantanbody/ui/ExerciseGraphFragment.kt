package hs.capstone.tantanbody.user

import android.annotation.SuppressLint
import android.graphics.Color
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

class ExerciseGraphFragment : Fragment() {
    val TAG = "ExerciseGraphFragment"
    lateinit var barChart: BarChart

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_exercise_graph, container, false)
        barChart = layout.findViewById(R.id.barChart)


        var i = 0
        var entries = ArrayList<BarEntry>()
        exercises.forEach { (weekday, minute) ->
            entries.add(BarEntry(i.toFloat(), minute.toFloat()))
            i++
        }

        // BarChart 설정하기
        var DSbar = BarDataSet(entries, getString(R.string.graph_label_exercise))
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
        lateinit var exercises: Map<String, Int>
        fun newInstance(exercises: Map<String, Int>): Fragment {
            this.exercises = exercises
            return ExerciseGraphFragment()
        }
    }
}