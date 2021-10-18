package hs.capstone.tantanbody.user

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.setPadding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import hs.capstone.tantanbody.R
import kotlin.collections.ArrayList

class WeightGraphFragment : Fragment() {
    val TAG = "WeightGraphFragment"
    lateinit var lineChart: LineChart

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_weight_graph, container, false)
        lineChart = layout.findViewById(R.id.lineChart)


        val axis_labels = listOf<String>("월","화","수","목","금","토","일")
        var entries = ArrayList<Entry>()
        for (i in 0..6) {
            entries.add(Entry(
                i.toFloat(),
                // 데이터가 없으면 0으로 채움
                (weights[axis_labels[i]] ?: 0).toFloat())
            )
        }

        // LineChart 설정하기
        var DSline = LineDataSet(entries, getString(R.string.graph_label_weight))
        DSline.color = getColorFrom(R.color.purple)
        DSline.valueTextColor = getColorFrom(R.color.unused_content)
        DSline.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
        DSline.lineWidth = 5f
        DSline.valueTextSize = 12f

        lineChart.xAxis.valueFormatter = IndexAxisValueFormatter(axis_labels)
        lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        lineChart.xAxis.isEnabled = true
        lineChart.axisRight.isEnabled = false
        lineChart.setExtraOffsets(0f,0f,15f,0f)
        lineChart.description.text = "kg(킬로그램)"
        lineChart.data = LineData(DSline)
        lineChart.invalidate()

        return layout
    }

    @SuppressLint("ResourceType")
    fun getColorFrom(address: Int): Int {
        return Color.parseColor(getString(address))
    }

    companion object {
        lateinit var weights: Map<String, Float>
        fun newInstance(userWeights: Map<String, Float>): Fragment {
            this.weights = userWeights
            return WeightGraphFragment()
        }
    }
}