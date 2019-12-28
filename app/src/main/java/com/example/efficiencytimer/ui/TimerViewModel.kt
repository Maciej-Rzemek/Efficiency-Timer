package com.example.efficiencytimer.ui

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class TimerViewModel : ViewModel() {

    enum class TimerState {Stopped, Paused, Running}
    private lateinit var timer: CountDownTimer
    private var timerState = TimerState.Stopped


    companion object {
        // Time has ended
        const val DONE = 0L
        // Value of one second
        const val ONE_SECOND = 1000L
        // This is the total time of the game
        const val COUNTDOWN_TIME = 60000L
    }


    private val _currentTime = MutableLiveData<Long>()
    val currentTime: LiveData<Long>
        get() = _currentTime

    val currentTimeString = Transformations.map(currentTime) { time ->
        DateUtils.formatElapsedTime(time)
    }

    fun countingStart() {
        startTimer()
        timerState = TimerState.Running
    }

    fun countingPaused() {
        timer.cancel()
        timerState = TimerState.Paused
    }

    fun countingStoped() {
        timer.cancel()
        _currentTime.value = DONE
        //timerState = TimerState.Stopped
        //onTimerFinished
    }

    fun countingResume() {

    }

    private fun startTimer() {
        timerState = TimerState.Running

        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {
            override fun onFinish() {
                _currentTime.value = DONE
            }

            override fun onTick(millisUntilFinished: Long) {
                _currentTime.value = (millisUntilFinished/ONE_SECOND)
            }
        }.start()
    }

    //
    override fun onCleared() {
        Log.i("TAG", "ViewModel Destroyed")
        timerState = TimerState.Paused
        super.onCleared()
    }
}


/*// Timer field

    // Timer default state
    private var timerState = TimerState.Stopped

    // The current time
    private val _currentTime = MutableLiveData<Long>()
    val currentTime : LiveData<Long>
        get() = _currentTime

    val currentTimeString = Transformations.map(currentTime) { time ->
        DateUtils.formatElapsedTime(time)
    }

    init {
        timer = object: CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {
            override fun onFinish() {
                _currentTime.value = DONE
            }

            override fun onTick(millisUntilFinished: Long) {
                _currentTime.value = (millisUntilFinished/ONE_SECOND)
            }

        }
    }*/
