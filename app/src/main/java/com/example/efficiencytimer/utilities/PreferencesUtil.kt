package com.example.efficiencytimer.utilities

import android.content.Context
import android.preference.PreferenceManager
import com.example.efficiencytimer.ui.TimerViewModel

class PreferencesUtil(context: Context) {

    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)


    companion object {

        private const val WORKING_TIMER_LENGTH_ID = "working_timer_length_key"

        // Takes the value of Working Time preferences SeekBar
        fun getWorkTimerLength(context: Context): Int {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getInt(WORKING_TIMER_LENGTH_ID, 25)
        }

        private const val BREAK_TIMER_LENGTH_ID = "break_timer_length_key"

        fun getBreakTimerLength(context: Context): Int{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getInt(BREAK_TIMER_LENGTH_ID, 5)
        }

        private const val PREVIOUS_TIMER_LENGTH_SECONDS_ID = "previous_working_timer_length_seconds"

        fun getPreviousWorkingTimerLengthSeconds(context: Context): Long{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID, 0)
        }

        fun setPreviousWorkingTimerLengthSeconds(seconds: Long, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID, seconds)
            editor.apply()
        }


        private const val SECONDS_REMAINING_ID = "seconds_remaining"

        fun getSecondsRemaining(context: Context): Long{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(SECONDS_REMAINING_ID, 0)
        }

        fun setSecondsRemaining(seconds: Long, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(SECONDS_REMAINING_ID, seconds)
            editor.apply()
        }

        private const val TIMER_STATE_ID = "timer_state_id"

        fun getTimerState(context: Context) : TimerViewModel.TimerState {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            val ordinal = preferences.getInt(TIMER_STATE_ID, 0)
            return TimerViewModel.TimerState.values()[ordinal]
        }

        fun setTimerState(state: TimerViewModel.TimerState, context: Context) {
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            val ordinal = state.ordinal
            editor.putInt(TIMER_STATE_ID, ordinal)
            editor.apply()
        }
    }
}