package hs.capstone.tantanbody.ui

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.core.view.children
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import hs.capstone.tantanbody.R
import kotlin.collections.ArrayList

class WeightGraphFragment : Fragment() {
    lateinit var lineChart: LineChart

    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_weight_graph, container, false)
        lineChart = layout.findViewById(R.id.lineChart)


        // LineChart 데이터 가져오기
        var weights = mapOf(
            0 to 23f,
            1 to 20f,
            2 to 30f,
            3 to 21f,
            4 to 16f,
            5 to 26f,
            6 to 18f
        )
        //lineChart.setNoDataText(getString(R.string.graph_no_data))

        var entries = ArrayList<Entry>()
        weights.forEach {
            entries.add(Entry(it.key.toFloat(), it.value))
        }

        // LineChart 설정하기
        var DSline = LineDataSet(entries, getString(R.string.graph_label_weight))

        DSline.color = getColorFrom(R.color.purple)
        DSline.valueTextColor = getColorFrom(R.color.unused_content)
        DSline.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
        DSline.lineWidth = 5f

        lineChart.xAxis.isEnabled = false
        lineChart.axisRight.isEnabled = false
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
        fun newInstance() = WeightGraphFragment()
    }
}