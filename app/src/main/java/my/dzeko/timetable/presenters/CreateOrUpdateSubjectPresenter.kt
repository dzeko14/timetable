package my.dzeko.timetable.presenters

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Parcelable
import android.text.TextUtils
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import my.dzeko.timetable.R
import my.dzeko.timetable.contracts.CreateOrUpdateSubjectContract
import my.dzeko.timetable.entities.Subject
import my.dzeko.timetable.interfaces.IModel
import my.dzeko.timetable.models.Model

class CreateOrUpdateSubjectPresenter(private var mView: CreateOrUpdateSubjectContract.View?) :
    CreateOrUpdateSubjectContract.Presenter {
    private val mModel: IModel = Model.getInstance()

    private var mGetScheduleFromDBDisposable: Disposable? = null

    private var mSubject: Subject? = null
    private var mDayId = 0
    private var mWeekId = 0

    private var mIsStateRestored = false

    override fun onUserClick(itemId: Int): Boolean {
        when (itemId) {
            R.id.submit_button -> {
                mView!!.saveData()
                return true
            }
        }
        return false
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        if (savedInstanceState == null) return

        mIsStateRestored = true

        mSubject = savedInstanceState.getParcelable(SUBJECT_IN_PRESENTER)
        mDayId = savedInstanceState.getInt(DAY_ID)
        mWeekId = savedInstanceState.getInt(WEEK_ID)
    }

    override fun saveState(): Bundle {
        val bundle = Bundle()
        bundle.putParcelable(SUBJECT_IN_PRESENTER, mSubject as Parcelable?)
        bundle.putInt(DAY_ID, mDayId)
        bundle.putInt(WEEK_ID, mWeekId)
        return bundle
    }

    override fun destroy() {
        mView = null
        if (mGetScheduleFromDBDisposable != null) {
            mGetScheduleFromDBDisposable!!.dispose()
        }
    }

    override fun onDataSaving(
        name: String, fullName: String, cabinet: String, teacher: String,
        position: Int, type: String
    ) {
        if (TextUtils.isEmpty(name)) return

        if (mSubject == null) {
            createSubject(name, fullName, cabinet, teacher, position, type)
        } else {
            updateSubject(name, fullName, cabinet, teacher, position, type)
        }
    }

    @SuppressLint("CheckResult")
    private fun updateSubject(
        name: String, fullName: String, cabinet: String,
        teacher: String, position: Int, type: String
    ) {
        Completable.fromAction {
            mSubject!!.subjectName = name
            if (!TextUtils.isEmpty(fullName)) mSubject!!.fullSubjectName = fullName
            else mSubject!!.fullSubjectName = name

            if (!TextUtils.isEmpty(cabinet)) mSubject!!.cabinet = cabinet

            if (!TextUtils.isEmpty(teacher)) mSubject!!.teacher = teacher

            mSubject!!.type = type

            mSubject!!.position = position
            mModel.saveSubject(mSubject)
        }
            .subscribeOn(Schedulers.io())
            .subscribe(object : DBSavingObserver() {
                override fun onError(e: Throwable) {
                    //TODO: Implement onError
                }
            })
    }

    @SuppressLint("CheckResult")
    private fun createSubject(
        name: String, fullName: String, cabinet: String,
        teacher: String, position: Int, type: String
    ) {
        Completable.fromAction {
            val subject = Subject(
                mDayId,
                mWeekId,
                mModel.selectedScheduleGroupName,
                name,
                (if (TextUtils.isEmpty(fullName)) name else fullName),
                cabinet,
                teacher,
                position,
                type
            )
            mModel.saveSubject(subject)
        }
            .subscribeOn(Schedulers.io())
            .subscribe(object : DBSavingObserver() {
                override fun onError(e: Throwable) {
                    //TODO: Implement onError
                }
            })
    }

    @SuppressLint("CheckResult")
    override fun onDataReceived(dayId: Int, weekId: Int, subjectId: Long) {
        if (mIsStateRestored) return
        mDayId = dayId
        mWeekId = weekId

        if (subjectId != -1L) {
            mView!!.showLoading()
            loadSubjectFromDB(subjectId)
        }
    }

    private fun loadSubjectFromDB(subjectId: Long) {
        mGetScheduleFromDBDisposable = Single.just(mModel)
            .map { model ->
                mSubject = model.getSubject(subjectId)
                mSubject
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<Subject?>() {
                override fun onSuccess(subject: Subject) {
                    mView!!.setSubject(subject)
                    mView!!.hideLoading()
                }

                override fun onError(e: Throwable) {
                    mView!!.hideLoading()
                }
            })
    }

    private abstract class DBSavingObserver : CompletableObserver {
        override fun onSubscribe(d: Disposable) {
            //not uses
        }

        override fun onComplete() {
            //not uses
        }
    }

    companion object {
        private const val SUBJECT_IN_PRESENTER = "subject in presenter"
        private const val DAY_ID = "day id"
        private const val WEEK_ID = "week id"
    }
}
