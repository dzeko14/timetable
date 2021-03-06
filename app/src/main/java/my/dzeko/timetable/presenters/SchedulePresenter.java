package my.dzeko.timetable.presenters;

import android.annotation.SuppressLint;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import my.dzeko.timetable.contracts.ScheduleContract;
import my.dzeko.timetable.interfaces.IModel;
import my.dzeko.timetable.entities.Schedule;
import my.dzeko.timetable.models.Model;
import my.dzeko.timetable.observers.ScheduleObservable;

public class SchedulePresenter implements ScheduleContract.Presenter {
    private ScheduleContract.View mView;
    private IModel mModel;

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public SchedulePresenter(ScheduleContract.View view) {
        this.mView = view;
        mModel = Model.getInstance();
        ScheduleObservable.getInstance().registerObserver(this);
    }

    @Override
    public boolean onUserClick(int itemId) {
        return false;
    }

    @Override
    public void destroy() {
        mView = null;
        ScheduleObservable.getInstance().unregisterObserver(this);
        mCompositeDisposable.dispose();
    }

    @SuppressLint("CheckResult")
    @Override
    public void onScheduleRequest() {
        mView.showLoading();

        mCompositeDisposable.add(
                requestScheduleCompletable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Schedule>() {
                    @Override
                    public void onSuccess(Schedule schedule) {
                        if (schedule.getSchedule().size() == 0){
                            mView.showEmptyScreen();
                        } else {
                            mView.hideEmptyScreen();
                            mView.updateSchedule(schedule);
                            mView.scrollToCurrentDay();
                        }
                        mView.hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                })
        );
    }

    private Single<Schedule> requestScheduleCompletable() {
        return Single.just(mModel).map(new Function<IModel, Schedule>() {
            @Override
            public Schedule apply(IModel model) throws Exception {
                return model.getSelectedSchedule();
            }
        });
    }

    @SuppressLint("CheckResult")
    @Override
    public void onSelectedScheduleChanged(final Schedule schedule) {
        if (schedule == null) return;
        else if (schedule.getSchedule().size() == 0) {
            showEmptyScreenCompletable().subscribeOn(AndroidSchedulers.mainThread()).subscribe();
            return;
        }
        updateScheduleCompletable(schedule).subscribeOn(AndroidSchedulers.mainThread()).subscribe();
    }

    private Completable updateScheduleCompletable(final Schedule schedule) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                mView.updateSchedule(schedule);
                mView.hideEmptyScreen();
            }
        });
    }

    private Completable showEmptyScreenCompletable(){
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                mView.showEmptyScreen();
            }
        });
    }
}
