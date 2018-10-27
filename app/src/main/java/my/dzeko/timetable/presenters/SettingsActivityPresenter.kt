package my.dzeko.timetable.presenters

import android.annotation.SuppressLint
import android.content.SharedPreferences
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import my.dzeko.timetable.R
import my.dzeko.timetable.contracts.SettingsActivityContract
import my.dzeko.timetable.contracts.SettingsActivityContract.View
import my.dzeko.timetable.entities.Schedule
import my.dzeko.timetable.interfaces.IModel
import my.dzeko.timetable.models.Model
import my.dzeko.timetable.observers.ScheduleObservable
import my.dzeko.timetable.utils.DateUtils
import my.dzeko.timetable.wrappers.SharedPreferencesWrapper

class SettingsActivityPresenter(var mView: View?): SettingsActivityContract.Presenter {
    val FIRST_WEEK_VALUE :String by lazy {
        mView?.context?.getString(R.string.first_week_prefs_value)!!
    }
    val SECOND_WEEK_VALUE :String by lazy {
        mView?.context?.getString(R.string.second_week_prefs_value)!!
    }


    override fun onUserClick(itemId: Int): Boolean {
        //Empty
        return true
    }

    override fun destroy() {
        mView = null
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when(key){
            mView?.context?.getString(R.string.weeks_prefs_key) -> updateKeyDate(sharedPreferences,
                    key!!)
        }
    }

    @SuppressLint("CheckResult")
    private fun updateKeyDate(sp: SharedPreferences?, key: String) {
        Completable.fromAction {
            val weekValue = sp?.getString(key, FIRST_WEEK_VALUE)

            val newKeyDate = when(weekValue){
                FIRST_WEEK_VALUE -> DateUtils.createKeyDate(true)
                SECOND_WEEK_VALUE -> DateUtils.createKeyDate(false)
                else -> return@fromAction
            }
            SharedPreferencesWrapper.getInstance().keyDate = newKeyDate

            ScheduleObservable.getInstance()
                    .notifySelectedScheduleChanged(Model.getInstance().selectedSchedule)
        }
                .subscribeOn(Schedulers.io())
                .subscribe()
    }
}