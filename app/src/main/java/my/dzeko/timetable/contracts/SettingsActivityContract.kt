package my.dzeko.timetable.contracts

import android.content.SharedPreferences
import my.dzeko.timetable.interfaces.IPresenter
import my.dzeko.timetable.interfaces.IView

class SettingsActivityContract {
    interface View : IView {

    }

    interface Presenter : IPresenter {
        fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?)
    }
}