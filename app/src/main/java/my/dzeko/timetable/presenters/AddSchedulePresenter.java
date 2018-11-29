package my.dzeko.timetable.presenters;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import my.dzeko.timetable.R;
import my.dzeko.timetable.contracts.AddScheduleContract;
import my.dzeko.timetable.interfaces.IModel;
import my.dzeko.timetable.entities.Schedule;
import my.dzeko.timetable.models.Model;
import my.dzeko.timetable.observers.ScheduleObservable;
import my.dzeko.timetable.services.ParseScheduleService;

public class AddSchedulePresenter implements AddScheduleContract.Presenter {
    private AddScheduleContract.View mView;
    private IModel mModel;

    public AddSchedulePresenter(AddScheduleContract.View view) {
        this.mView = view;
        mModel = Model.getInstance();
        ScheduleObservable.getInstance().registerObserver(this);
    }

    @Override
    public boolean onUserClick(int itemId) {
        switch (itemId) {
            case R.id.confirmGroupName_button_addGroupActivity:
                parseGroupSchedule();
                return true;
            case R.id.createGroup_button_addGroupActivity:
                choseCurrentWeekNumberBeforeCreating();
                return true;
        }
        return false;
    }

    private void parseGroupSchedule() {
        String groupName = mView.getGroupName().toUpperCase();
        if(TextUtils.isEmpty(groupName)) {
            mView.showEmptyGroupName();
            return;
        }
        mView.showLoading();
        mView.startService(groupName, ParseScheduleService.class);
    }

    private void choseCurrentWeekNumberBeforeCreating(){
        final String groupName = mView.getGroupName().toUpperCase();
        if(TextUtils.isEmpty(groupName)) {
            mView.showEmptyGroupName();
            return;
        }

        mView.showChoseWeekNumberDialog();
    }

    @Override
    @SuppressLint("CheckResult")
    public void createGroupSchedule() {
        final String groupName = mView.getGroupName().toUpperCase();
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                mModel.saveGroup(groupName);
            }
        })
                .subscribeOn(Schedulers.io())
                .subscribe();
        mView.notifyScheduleCreated();
    }

    @Override
    public void destroy() {
        mView = null;
        ScheduleObservable.getInstance().unregisterObserver(this);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onSelectedScheduleChanged(Schedule schedule) {
        if (schedule == null){
            Completable.fromAction(new Action() {
                @Override
                public void run() throws Exception {
                    mView.showParseError();
                    mView.hideLoading();
                }
            }).subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe();
            return;
        }
        mView.close();
    }

    @SuppressLint("CheckResult")
    @Override
    public void setCurrentWeek(final int which) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                mModel.setCurrentWeek(which == 0);
            }
        })
                .subscribeOn(Schedulers.io())
                .subscribe();
    }
}
