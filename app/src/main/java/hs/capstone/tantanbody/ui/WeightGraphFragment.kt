package hs.capstone.tantanbody.user

import android.annotation.SuppressLint
import android.graphics.Color
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

    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_weight_graph, container, false)
        lineChart = layout.findViewById(R.id.lineChart)


        var i = 0
        var entries = ArrayList<Entry>()
        weights.forEach { (weekday, kg) ->
            entries.add(Entry(i.toFloat(), kg))
            i++
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
        lateinit var weights: Map<String, Float>
        fun newInstance(userWeights: Map<String, Float>): Fragment {
            this.weights = userWeights
            return WeightGraphFragment()
        }
    }
}