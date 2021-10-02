package hs.capstone.tantanbody.user

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import hs.capstone.tantanbody.R
import hs.capstone.tantanbody.model.TTBApplication
import hs.capstone.tantanbody.user.WeightAddActivity

class TabRecordFragment : Fragment() {
    val TAG = "TabRecordFragment"
    lateinit var app: Application
    lateinit var goalTitleTv: TextView
    lateinit var fitnessGraphTitle: TextView
    lateinit var weightGraphTitle: TextView
    lateinit var goalBrief: TextView
    lateinit var addWeightBtn: Button

    private val recordedViewModel by viewModels<UserViewModel> {
        UserViewModelFactory((app as TTBApplication).userRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_tab_record, container, false)
        goalTitleTv = layout.findViewById(R.id.goalTitleTv)
        fitnessGraphTitle = layout.findViewById(R.id.fitnessGraphTitle)
        weightGraphTitle = layout.findViewById(R.id.weightGraphTitle)
        goalBrief = layout.findViewById(R.id.goalBrief)
        addWeightBtn = layout.findViewById(R.id.addWeightBtn)

        goalTitleTv.setOnClickListener {
            buildEditingGoalDialog().show()
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
        addWeightBtn.setOnClickListener {
            val intent = Intent(context, WeightAddActivity::class.java)
            startActivity(intent)
        }
        fitnessGraphTitle.performClick()
        return layout
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        this.app = context.applicationContext as Application
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

    fun buildEditingGoalDialog(): AlertDialog.Builder {
        val goalEt = EditText(context)
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
                Log.d(TAG, "goalEt.text: ${goalEt.text}")

                recordedViewModel.goal = goalEt.text.toString()
                setGoalBriefUI(recordedViewModel.goal)
            }
        )
        return goalDlg
    }

    fun setGoalBriefUI(goal: String?) {
        val form = " %s 님의 운동목표는\n \"%s\" 입니다."
        if (goal.isNullOrEmpty()) {
            goalBrief.visibility = View.INVISIBLE
        } else {
            goalBrief.text = form.format(recordedViewModel.LoginUser?.displayName, goal)

        }
    }

    companion object {
        fun newInstance(): Fragment {
            return TabRecordFragment()
        }
    }
}