package my.dzeko.timetable.fragments

import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import my.dzeko.timetable.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.settings_fragment_pref_screen)

    }
}