package my.dzeko.timetable.contracts

import android.content.SharedPreferences
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceScreen
import my.dzeko.timetable.interfaces.IPresenter
import my.dzeko.timetable.interfaces.IView

abstract class SettingsFragmentContract {
    interface View :IView, SharedPreferences.OnSharedPreferenceChangeListener {
        fun changeWeekSummary(prefIndex :Int, summary :String)
    }

    interface Presenter :IPresenter {
        fun onPreferenceScreenCreated(prefScreen :PreferenceScreen)
        fun onPreferenceScreenChanged(sharedPreferences: SharedPreferences?,
                                      preference :Preference, key :String?)
    }
}