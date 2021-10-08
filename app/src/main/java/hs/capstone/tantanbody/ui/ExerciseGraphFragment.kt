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
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
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

        val axis_labels = listOf<String>("월","화","수","목","금","토","일")
        var entries = ArrayList<BarEntry>()
        for (i in 0..6) {
            entries.add(BarEntry(
                i.toFloat(),
                (exercises[axis_labels[i]] ?: 0).toFloat())
            )
        }
        Log.d(TAG, "exercises: ${exercises}")
        Log.d(TAG, "entries: ${entries}")

        // BarChart 설정하기
        var DSbar = BarDataSet(entries, getString(R.string.graph_label_exercise))
        DSbar.color = getColorFrom(R.color.orange)
        DSbar.valueTextColor = getColorFrom(R.color.unused_content)
        DSbar.valueTextSize = 10f

        barChart.xAxis.valueFormatter = IndexAxisValueFormatter(axis_labels)
        barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        barChart.xAxis.isEnabled = true
        barChart.axisRight.isEnabled = false
        barChart.setFitBars(true)
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