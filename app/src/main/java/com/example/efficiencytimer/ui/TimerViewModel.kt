package com.example.efficiencytimer.ui

import android.content.Context
import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.efficiencytimer.utilities.PreferencesUtil
import com.example.efficiencytimer.utilities.TimerState
import com.example.efficiencytimer.utilities.WorkState

class TimerViewModel(private val context: Context) : ViewModel() {

    private lateinit var timer: CountDownTimer
    private var timerState = MutableLiveData<TimerState>()
    private var workState = MutableLiveData<WorkState>()
    private var timerWorkLengthSeconds = 60L
    private var timerBreakLengthSeconds = 60L

    companion object {
        // Time has ended
        const val DONE = 0L
        // Value of one second
        const val ONE_SECOND = 1000L
    }

    private val _currentTime = MutableLiveData<Long>()
    val currentTime: LiveData<Long>
        get() = _currentTime



    fun countingStart() {
        startTimer()
        timerState.value = TimerState.Running
    }

    fun countingPaused() {
        timer.cancel()
        timerState.value = TimerState.Paused
        Log.i("TAG", "Initialized countingPaused() - WorkState = ${workState.value} - TimerState = ${timerState.value}")
    }

    fun initTimer() {
        val timerWorkLength = PreferencesUtil.getWorkTimerLength(context)
        val timerBreakLength = PreferencesUtil.getBreakTimerLength(context)
        timerWorkLengthSeconds = timerWorkLength * 60L
        timerBreakLengthSeconds = timerBreakLength * 60L
    }

    private fun startTimer() {

        // If workState == Stopped or Null then set workState to working
        if (workState.value == WorkState.Stopped || workState.value == null) {
            workState.value = WorkState.Working
        }

        var secondsLeft: Long =
            // resuming timer _currentTime has already value
            if (_currentTime.value != null && _currentTime.value!! > 0 ) {
                Log.i("TAG", "Resuming timer - secondsLeft = ${_currentTime.value} - WorkState = ${workState.value} - TimerState = ${timerState.value}")
                _currentTime.value!!
            } else {
                when (workState.value) {
                    WorkState.Working -> {
                        Log.i("TAG", "WorkState.Working -> timerWorkLengthSeconds - WorkState = ${workState.value} - TimerState = ${timerState.value}")
                        timerWorkLengthSeconds
                    }
                    WorkState.Resting -> {
                        Log.i("TAG", "WorkState.Resting -> timerBreakLengthSeconds - WorkState = ${workState.value} - TimerState = ${timerState.value}")
                        timerBreakLengthSeconds
                    }
                    else -> {
                        Log.i("TAG", "else -> timerWorkLengthSeconds")
                        timerWorkLengthSeconds
                    }
                }
            }

        timerState.value = TimerState.Running

        timer = object : CountDownTimer(secondsLeft * 1000, ONE_SECOND) {
            override fun onFinish() {
                _currentTime.value = DONE
                when (workState.value) {
                    WorkState.Working -> {
                        startTimer()
                        workState.value = WorkState.Resting
                    }
                    WorkState.Resting -> {
                        startTimer()
                        workState.value = WorkState.Working
                    }
                }
            }

            override fun onTick(millisUntilFinished: Long) {
                _currentTime.value = (millisUntilFinished/ONE_SECOND)
            }
        }.start()
    }

    fun onSkipTimerSession() {
        if (workState.value == WorkState.Working) {
            workState.value = WorkState.Resting
            _currentTime.value = timerBreakLengthSeconds
            Log.i("TAG", "onSkipTimerSession - was working and skipped to break - ${workState.value}")
        } else {
            workState.value = WorkState.Working
            _currentTime.value = timerWorkLengthSeconds
            Log.i("TAG", "onSkipTimerSession - break has been skipped to work - ${workState.value}")
        }
        timer.cancel()
    }

    fun getCurrentTime() = _currentTime

    fun getTimerWorkLengthSeconds() = timerWorkLengthSeconds

    fun getWorkState() = workState


    override fun onCleared() {
        Log.i("TAG", "ViewModel Destroyed")
        timerState.value = TimerState.Paused
        super.onCleared()
    }
}
