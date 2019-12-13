package com.example.efficiencytimer.ui

import android.os.CountDownTimer
import android.text.format.DateUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class TimerViewModel : ViewModel() {

    companion object {
        // Time has ended
        const val DONE = 0L
        // Value of one second
        const val ONE_SECOND = 1000L
        // This is the total time of the game
        const val COUNTDOWN_TIME = 60000L
    }

    // Timer field
    private val timer: CountDownTimer

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
        timer.start()
    }

}
