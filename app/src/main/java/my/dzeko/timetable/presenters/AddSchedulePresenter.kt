package my.dzeko.timetable.presenters

import android.annotation.SuppressLint
import android.text.TextUtils
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import my.dzeko.timetable.R
import my.dzeko.timetable.contracts.AddScheduleContract
import my.dzeko.timetable.entities.Schedule
import my.dzeko.timetable.interfaces.IModel
import my.dzeko.timetable.models.Model
import my.dzeko.timetable.observers.ScheduleObservable
import my.dzeko.timetable.services.ParseScheduleService
import java.util.*

class AddSchedulePresenter(private var mView: AddScheduleContract.View?) :
    AddScheduleContract.Presenter {
    private val mModel: IModel = Model.getInstance()

    init {
        ScheduleObservable.getInstance().registerObserver(this)
    }

    override fun onUserClick(itemId: Int): Boolean {
        when (itemId) {
            R.id.confirmGroupName_button_addGroupActivity -> {
                parseGroupSchedule()
                mView!!.hideKeyBoard()
                return true
            }

            R.id.createGroup_button_addGroupActivity -> {
                choseCurrentWeekNumberBeforeCreating()
                mView!!.hideKeyBoard()
                return true
            }
        }
        return false
    }

    private fun parseGroupSchedule() {
        val groupName = mView!!.groupName.uppercase(Locale.getDefault())
        if (TextUtils.isEmpty(groupName)) {
            mView!!.showEmptyGroupName()
            return
        }
        mView!!.showLoading()
        mView!!.startService(groupName, ParseScheduleService::class.java)
    }

    private fun choseCurrentWeekNumberBeforeCreating() {
        val groupName = mView!!.groupName.uppercase(Locale.getDefault())
        if (TextUtils.isEmpty(groupName)) {
            mView!!.showEmptyGroupName()
            return
        }

        mView!!.showChoseWeekNumberDialog()
    }

    @SuppressLint("CheckResult")
    override fun createGroupSchedule() {
        val groupName = mView!!.groupName.uppercase(Locale.getDefault())
        Completable.fromAction { mModel.saveGroup(groupName) }
            .subscribeOn(Schedulers.io())
            .subscribe()
        mView!!.notifyScheduleCreated()
    }

    override fun destroy() {
        mView = null
        ScheduleObservable.getInstance().unregisterObserver(this)
    }

    @SuppressLint("CheckResult")
    override fun onSelectedScheduleChanged(schedule: Schedule) {
        if (schedule == null) {
            Completable.fromAction {
                mView!!.showParseError()
                mView!!.hideLoading()
            }.subscribeOn(AndroidSchedulers.mainThread())
                .subscribe()
            return
        }
        mView!!.close()
    }

    @SuppressLint("CheckResult")
    override fun setCurrentWeek(which: Int) {
        Completable.fromAction { mModel.setCurrentWeek(which == 0) }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }
}
