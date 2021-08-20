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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "weights"

/**
 * A simple [Fragment] subclass.
 * Use the [WeightGraphFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WeightGraphFragment : Fragment() {
    val TAG = "WeightGraphFragment"
    lateinit var lineChart: LineChart
    lateinit var weights: MutableList<Float>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            weights = it.getFloatArray(ARG_PARAM1) as MutableList<Float>
            Log.d(TAG, "javaClass: ${weights?.javaClass}  isNullOrEmpty: ${weights?.isNullOrEmpty()}")

//            weights?.toMutableList()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val layout = inflater.inflate(R.layout.fragment_weight_graph, container, false)
        lineChart = layout.findViewById(R.id.lineChart)

        // LineChart 데이터 가져오기
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
        var weights = mutableListOf(23f, 20f, 30f, 21f, 16f, 26f, 18f)

        fun newInstance(weights: FloatArray) =
                WeightGraphFragment().apply {
                    arguments = Bundle().apply {
                        // FloatArray형을 보냄(key, value)
                        putFloatArray(ARG_PARAM1, weights)
                    }
                }
    }
}