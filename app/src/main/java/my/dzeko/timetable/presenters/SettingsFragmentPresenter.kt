package my.dzeko.timetable.presenters

import android.content.SharedPreferences
import android.support.v7.preference.ListPreference
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceScreen
import my.dzeko.timetable.R
import my.dzeko.timetable.contracts.SettingsFragmentContract

class SettingsFragmentPresenter(var mView :SettingsFragmentContract.View?)
    :SettingsFragmentContract.Presenter {

    override fun onPreferenceScreenCreated(prefScreen: PreferenceScreen) {
        val count = prefScreen.preferenceCount
        val sharedPreferences = prefScreen.sharedPreferences
        for (i in 0 until count){
            val preference = prefScreen.getPreference(i)
            val value = sharedPreferences.getString(preference.key, "")
            setPreferenceSummary(preference, value)
        }
    }

    override fun onUserClick(itemId: Int): Boolean {
        TODO("not implemented")
    }

    override fun destroy() {
        mView = null
    }

    private fun setPreferenceSummary(preference :Preference, value :String) {
        when(preference){
            is ListPreference -> {
                val prefIndex = preference.findIndexOfValue(value)
                preference.setSummary(preference.entries[prefIndex])
            }
        }
    }

    override fun onPreferenceScreenChanged(sharedPreferences: SharedPreferences?,
                                           preference: Preference, key: String?) {
        when(key){
            mView?.context?.getString(R.string.weeks_prefs_key) -> {
                val value = sharedPreferences?.getString(key, " ")

                value?.let {
                    setPreferenceSummary(preference, value)
                }
            }
        }


    }
}