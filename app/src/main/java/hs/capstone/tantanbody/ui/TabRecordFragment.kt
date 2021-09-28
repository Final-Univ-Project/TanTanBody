package hs.capstone.tantanbody.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Application
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.marginTop
import androidx.fragment.app.viewModels
import hs.capstone.tantanbody.R
import hs.capstone.tantanbody.model.TTBApplication
import hs.capstone.tantanbody.viewmodel.RecordedViewModel
import hs.capstone.tantanbody.viewmodel.RecordedViewModelFactory

class TabRecordFragment : Fragment() {
    val TAG = "TabRecordFragment"
    lateinit var goalTitleTv: TextView
    lateinit var fitnessGraphTitle: TextView
    lateinit var weightGraphTitle: TextView

    private val recordedViewModel by viewModels<RecordedViewModel> {
        RecordedViewModelFactory((app as TTBApplication).userRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_tab_record, container, false)
        goalTitleTv = layout.findViewById(R.id.goalTitleTv)
        fitnessGraphTitle = layout.findViewById(R.id.fitnessGraphTitle)
        weightGraphTitle = layout.findViewById(R.id.weightGraphTitle)

        goalTitleTv.setOnClickListener {
            val goalEt = EditText(this.context)
            goalEt.hint = getString(R.string.goal_title)

            val goalDlg: AlertDialog.Builder = AlertDialog.Builder(
                this.context,
                android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth
            )
            goalDlg.setTitle(R.string.goal_dialog_title)
            goalDlg.setView(goalEt)
            goalDlg.setPositiveButton(
                "확인",
                DialogInterface.OnClickListener { dialog, which ->
                    // ViewModel에 운동 목표 저장

                    goalTitleTv.text = goalEt.text
                }
            )
            goalDlg.show()
            Log.d(TAG, "R.id.goalTitle 클릭")
        }
        fitnessGraphTitle.setOnClickListener {
            loadGraphFragment(FitnessGraphFragment.newInstance())
            fitnessGraphTitle.setTextColor(getColorFrom(R.color.using_content))
            weightGraphTitle.setTextColor(getColorFrom(R.color.unused_content))
            Log.d(TAG, "R.id.recordFitnessGraph 클릭")
        }
        weightGraphTitle.setOnClickListener {
            var weights = recordedViewModel.getWeights()
            Log.d(TAG, "weights: ${weights}")

            loadGraphFragment(WeightGraphFragment.newInstance(weights))
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

    fun loadGraphFragment(fragment: Fragment) {
        var fragTranser = parentFragmentManager.beginTransaction()
        fragTranser.replace(R.id.recordGraphFragment, fragment)
        fragTranser.setReorderingAllowed(true)
        fragTranser.commit()
    }

    companion object {
        lateinit var app: Application
        fun newInstance(app: Application): Fragment {
            this.app = app
            return TabRecordFragment()
        }
    }
}