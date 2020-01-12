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
    private var stopSession : Boolean = false

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

    fun countingStopped() {
        timerState.value = TimerState.Stopped
        onFinishedTimerSession()
    }


    fun initTimer() {
        val timerWorkLength = PreferencesUtil.getWorkTimerLength(context)
        val timerBreakLength = PreferencesUtil.getBreakTimerLength(context)
        timerWorkLengthSeconds = timerWorkLength * 8L
        timerBreakLengthSeconds = timerBreakLength * 4L
    }

    private fun startTimer() {
        Log.i("TAG", "startTimer() is launched WorkState is ${workState.value}, and _currentTime value is ${_currentTime.value}")

        // If workState == Stopped or Null then set workState to working
        if (workState.value == WorkState.Stopped || workState.value == null) {
            workState.value = WorkState.Working
        }

        timerState.value = TimerState.Running

        var secondsLeft: Long =
            // resuming timer _currentTime has already value
            if (_currentTime.value != null && _currentTime.value!! > 0 ) {
                Log.i("TAG", "Resuming timer - secondsLeft = ${_currentTime.value} - WorkState = ${workState.value} - TimerState = ${timerState.value}")
                _currentTime.value!!
            } else {
                // _currentTime.value = 0
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

        timer = object : CountDownTimer(secondsLeft * 1000, ONE_SECOND) {
            override fun onFinish() {
                _currentTime.value = DONE
            }

            override fun onTick(millisUntilFinished: Long) {
                _currentTime.value = (millisUntilFinished/ONE_SECOND)
            }
        }.start()
    }

    fun getCurrentTime() = _currentTime

    fun getTimerWorkLengthSeconds() = timerWorkLengthSeconds

    fun getWorkState() = workState

    private fun onFinishedTimerSession() {
        initTimer()

        if (workState.value == WorkState.Working && !stopSession) { // was working and break has started
            workState.value = WorkState.Resting
            _currentTime.value = timerBreakLengthSeconds
            Log.i("TAG", "onFinishedTimerSession - was working and break has started - ${workState.value}")
        } else if (workState.value == WorkState.Resting && !stopSession) { // break has ended. new work session has started
            workState.value = WorkState.Working
            _currentTime.value = timerWorkLengthSeconds
            Log.i("TAG", "onFinishedTimerSession - break has ended. new work session has started - ${workState.value}")
        } else if (workState.value == WorkState.Working) { // Pause has been pressed?
            workState.value = WorkState.Stopped
            _currentTime.value = timerWorkLengthSeconds
            Log.i("TAG", "onFinishedTimerSession - Pause has been pressed? - ${workState.value}")
        } else {
            workState.value = WorkState.Stopped
            _currentTime.value = timerWorkLengthSeconds
            Log.i("TAG", "onFinishedTimerSession - else - ${workState.value}")
        }

        stopSession = false
        timer.cancel()

        Log.i("TAG", "onFinishedTimerSession - function has ended - ${workState.value}")
    }

    override fun onCleared() {
        Log.i("TAG", "ViewModel Destroyed")
        timerState.value = TimerState.Paused
        super.onCleared()
    }
}
