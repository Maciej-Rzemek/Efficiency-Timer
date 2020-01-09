package com.example.efficiencytimer.ui

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.efficiencytimer.R
import com.example.efficiencytimer.databinding.TimerFragmentBinding
import com.example.efficiencytimer.utilities.PreferencesUtil
import kotlinx.android.synthetic.main.timer_fragment.*

class TimerFragment : Fragment() {



    private lateinit var viewModel: TimerViewModel

    private lateinit var binding: TimerFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.timer_fragment, container, false)
        val viewModelFactory = TimerViewModelFactory(context!!)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(TimerViewModel::class.java)
        binding.timerViewModel = viewModel
        binding.lifecycleOwner = this


        viewModel.getCurrentTime().observe(this, Observer {
            if (it != null) {
                var minutes = it / 60
                var seconds = it % 60

                if (seconds > 10) {
                    timer_textView.text = "$minutes:$seconds"
                } else {
                    timer_textView.text = "$minutes:0$seconds"
                }
            }
        })

        return binding.root
        // return inflater.inflate(R.layout.timer_fragment, container, false)
    }

    private fun setCurrentTime() {

    }

    private fun initUI() {
        var time = viewModel.getTimerWorkLengthSeconds()
        var minutes = time / 60
        var seconds = time % 60

        timer_textView.text = "$minutes:0$seconds"
    }

    override fun onStart() {
        viewModel.initTimer()
        initUI()
        super.onStart()
    }

    override fun onPause() {
        Log.i("TAG", "TimerFragment PAUSED")
        super.onPause()
    }

    override fun onResume() {
        Log.i("TAG", "TimerFragment RESUMED")
        super.onResume()
    }


}
