package my.dzeko.timetable.activities

import android.content.Context
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import my.dzeko.timetable.R
import my.dzeko.timetable.contracts.SettingsActivityContract
import my.dzeko.timetable.presenters.SettingsActivityPresenter
import my.dzeko.timetable.wrappers.SharedPreferencesWrapper

class SettingsActivity : AppCompatActivity(), SettingsActivityContract.View,
SharedPreferences.OnSharedPreferenceChangeListener{
    val mPresenter: SettingsActivityContract.Presenter = SettingsActivityPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        SharedPreferencesWrapper.getInstance().registerChangeListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        SharedPreferencesWrapper.getInstance().unregisterChangeListener(this)
    }

    override fun showLoading() {
        //Empty
    }

    override fun hideLoading() {
        //Empty
    }

    override fun getContext(): Context {
        return applicationContext
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        mPresenter.onSharedPreferenceChanged(sharedPreferences, key)
    }
}
