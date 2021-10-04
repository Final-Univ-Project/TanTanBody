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
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import hs.capstone.tantanbody.R
import hs.capstone.tantanbody.model.TTBApplication

class TabUserFragment : Fragment() {
    val TAG = "TabRecordFragment"
    lateinit var app: Application
    lateinit var goalTitleTv: TextView
    lateinit var exerciseGraphTitle: TextView
    lateinit var weightGraphTitle: TextView
    lateinit var goalBrief: TextView
    lateinit var addWeightBtn: Button

    private val recordedViewModel by viewModels<UserViewModel> {
        UserViewModelFactory((app as TTBApplication).userRepository)
    }
    private lateinit var userWeights: Map<String, Float>
    private lateinit var userExercises: Map<String, Int>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.app = context.applicationContext as Application
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_tab_user, container, false)
        goalTitleTv = layout.findViewById(R.id.goalTitleTv)
        exerciseGraphTitle = layout.findViewById(R.id.exerciseGraphTitle)
        weightGraphTitle = layout.findViewById(R.id.weightGraphTitle)
        goalBrief = layout.findViewById(R.id.goalBrief)
        addWeightBtn = layout.findViewById(R.id.addWeightBtn)

        goalTitleTv.setOnClickListener {
            buildEditingGoalDialog().show()
            Log.d(TAG, "R.id.goalTitle 클릭")
        }
        exerciseGraphTitle.setOnClickListener {
            loadGraphFragment(ExerciseGraphFragment.newInstance(userExercises))
            exerciseGraphTitle.setTextColor(getColorFrom(R.color.using_content))
            weightGraphTitle.setTextColor(getColorFrom(R.color.unused_content))
            Log.d(TAG, "R.id.recordFitnessGraph 클릭")
        }
        weightGraphTitle.setOnClickListener {
            loadGraphFragment(WeightGraphFragment.newInstance(userWeights))
            weightGraphTitle.setTextColor(getColorFrom(R.color.using_content))
            exerciseGraphTitle.setTextColor(getColorFrom(R.color.unused_content))
            Log.d(TAG, "R.id.recordWeightGraph 클릭")
        }
        addWeightBtn.setOnClickListener {
            val intent = Intent(context, WeightAddActivity::class.java)
            startActivity(intent)
        }

        userWeights = recordedViewModel.userWeights.value ?: mapOf()
        userExercises = recordedViewModel.exerciseTimes.value ?: mapOf()
        recordedViewModel.goal.observe(viewLifecycleOwner, Observer { goal ->
            setGoalBriefUI(goal)
        })
        recordedViewModel.userWeights.observe(viewLifecycleOwner, Observer { items ->
            userWeights = items
        })
        recordedViewModel.exerciseTimes.observe(viewLifecycleOwner, Observer { items ->
            userExercises = items
        })

        exerciseGraphTitle.performClick()
        return layout
    }

    @SuppressLint("ResourceType")
    fun getColorFrom(address: Int): Int {
        return Color.parseColor(getString(address))
    }

    fun loadGraphFragment(fragment: Fragment) {
        var fragTranser = parentFragmentManager.beginTransaction()
        fragTranser.replace(R.id.userGraphFragment, fragment)
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
                recordedViewModel.goal.switchMap { goal ->
                    liveData { emit(goalEt.text.toString()) }
                }
                setGoalBriefUI(recordedViewModel.goal.value)
                Log.e(TAG, "model.goal: ${recordedViewModel.goal.value}")
            }
        )
        return goalDlg
    }

    fun setGoalBriefUI(goal: String?) {
        val form = " %s 님의 운동목표는\n \"%s\" 입니다."
        if (goal.isNullOrEmpty()) {
            goalBrief.visibility = View.GONE
        } else {
            goalBrief.text = form.format(recordedViewModel.LoginUser?.displayName, goal)
        }
    }

    companion object {
        fun newInstance(): Fragment {
            return TabUserFragment()
        }
    }
}