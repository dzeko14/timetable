package my.dzeko.timetable.presenters

import android.annotation.SuppressLint
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import my.dzeko.timetable.contracts.SelectSubjectContract
import my.dzeko.timetable.models.AbstractModel

class SelectSubjectPresenter(private var mView :SelectSubjectContract.View?) : SelectSubjectContract.Presenter {
    private val model = AbstractModel.getModel()

    @SuppressLint("CheckResult")
    override fun onViewCreated() {
        Single.just(model)
                .map { model ->
                    model.selectedSchedule
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { schedule ->
                    mView?.setSchedule(schedule)
                }
       // mView?.setSchedule(model.selectedSchedule)
    }

    override fun onUserClick(itemId: Int): Boolean {
        TODO("not implemented")
    }

    override fun destroy() {
        mView = null
    }
}