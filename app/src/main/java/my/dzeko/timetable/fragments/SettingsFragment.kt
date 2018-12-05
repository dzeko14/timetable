package my.dzeko.timetable.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import my.dzeko.timetable.R
import my.dzeko.timetable.contracts.SettingsFragmentContract
import my.dzeko.timetable.presenters.SettingsFragmentPresenter
import my.dzeko.timetable.wrappers.SharedPreferencesWrapper

class SettingsFragment : PreferenceFragmentCompat(), SettingsFragmentContract.View {
    val mPresenter :SettingsFragmentContract.Presenter = SettingsFragmentPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SharedPreferencesWrapper.getInstance().registerChangeListener(this)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.settings_fragment_pref_screen)
        mPresenter.onPreferenceScreenCreated(preferenceScreen)
    }

    override fun onDestroy() {
        super.onDestroy()
        SharedPreferencesWrapper.getInstance().unregisterChangeListener(this)
        mPresenter.destroy()
    }

    override fun changeWeekSummary(prefIndex: Int, summary: String) {
        val preference = preferenceScreen.getPreference(prefIndex)
        preference.summary = summary
    }

    override fun showLoading() {
        //Empty
    }

    override fun hideLoading() {
        //Empty
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        val preference = findPreference(key)
        preference?.let {
            mPresenter.onPreferenceScreenChanged(sharedPreferences, preference, key)
        }
    }
}