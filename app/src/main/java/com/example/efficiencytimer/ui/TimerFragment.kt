package com.example.efficiencytimer.ui

import android.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.health.TimerStat
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.efficiencytimer.R
import com.example.efficiencytimer.databinding.TimerFragmentBinding
import com.example.efficiencytimer.utilities.TimerState
import com.example.efficiencytimer.utilities.WorkState
import kotlinx.android.synthetic.main.skipping_session_dialog.view.*
import kotlinx.android.synthetic.main.timer_fragment.*

class TimerFragment : Fragment() {

    private lateinit var viewModel: TimerViewModel
    private lateinit var workState: WorkState
    private lateinit var timerState: TimerState
    private lateinit var binding: TimerFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.timer_fragment, container, false)
        val viewModelFactory = TimerViewModelFactory(context!!)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(TimerViewModel::class.java)
        binding.timerViewModel = viewModel
        binding.lifecycleOwner = this

        skippingSessionDialog()
        getObservers()

        return binding.root
    }

    private fun initUI() {
        var time = viewModel.getTimerWorkLengthSeconds()
        var minutes = time / 60
        var seconds = time % 60

        timer_textView.text = "$minutes:0$seconds"
    }

    private fun getObservers() {

        viewModel.getCurrentTime().observe(this, Observer {
            if (it != null) {
                var minutes = it / 60
                var seconds = it % 60

                if (seconds > 9) {
                    timer_textView.text = "$minutes:$seconds"
                } else {
                    timer_textView.text = "$minutes:0$seconds"
                }
            }
        })

        viewModel.getWorkState().observe(this, Observer {
            workState = it!!
            updateWorkStateTextView()
        })

        viewModel.getTimerState().observe(this, Observer {
            timerState = it!!
            updateButtons()
        })
    }

    private fun updateWorkStateTextView() {
        when (workState) {
            WorkState.Working -> {
                work_status_textView.text = "WORK"
            }
            WorkState.Resting -> {
                work_status_textView.text = "BREAK"
            }
            else -> {
                work_status_textView.text = "WORK"
            }
        }
    }

    private fun skippingSessionDialog() {
        binding.stopFab.setOnClickListener {
            val skippingSessionDialogView = LayoutInflater.from(context).inflate(R.layout.skipping_session_dialog, null)
            val dialogBuilder = AlertDialog.Builder(context)
                .setView(skippingSessionDialogView)
                .setTitle(R.string.skipping_workSession_dialog)
            val skippingSessionDialog = dialogBuilder.show()

            skippingSessionDialogView.confirmSkipFAB.setOnClickListener {
                viewModel.onSkipTimerSession()
                skippingSessionDialog.dismiss()
                if (timerState == TimerState.Running) {

                }
            }
            skippingSessionDialogView.cancelSkipFAB.setOnClickListener {
                skippingSessionDialog.dismiss()
            }
        }
    }

    private fun updateButtons() {
        when (timerState) {
            TimerState.Running -> {
                binding.playFab.visibility = View.INVISIBLE
                binding.stopFab.visibility = View.VISIBLE
                binding.pauseFab.visibility = View.VISIBLE

            }
            TimerState.Paused -> {
                binding.playFab.visibility = View.VISIBLE
                binding.stopFab.visibility = View.VISIBLE
                binding.pauseFab.visibility = View.INVISIBLE

            }
        }
    }


    override fun onStart() {
        Log.i("FragmentTAG", "onStart Initialized")
        viewModel.initTimer()
        initUI()
        super.onStart()
    }

    override fun onPause() {
        Log.i("FragmentTAG", "TimerFragment PAUSED")
        super.onPause()
    }

    override fun onResume() {
        Log.i("FragmentTAG", "onResume Initialized")
        super.onResume()
    }


}
