package com.example.efficiencytimer.ui

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.efficiencytimer.R
import com.example.efficiencytimer.databinding.TimerFragmentBinding

class TimerFragment : Fragment() {

    companion object {
        fun newInstance() = TimerFragment()
    }

    private lateinit var viewModel: TimerViewModel

    private lateinit var binding: TimerFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.timer_fragment, container, false)
        viewModel = ViewModelProviders.of(this).get(TimerViewModel::class.java)
        binding.timerViewModel = viewModel
        binding.lifecycleOwner = this


        return binding.root
        // return inflater.inflate(R.layout.timer_fragment, container, false)
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
